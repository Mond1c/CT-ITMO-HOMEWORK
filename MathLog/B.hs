{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use void" #-}
{-# HLINT ignore "Use second" #-}
{-# LANGUAGE ScopedTypeVariables, InstanceSigs #-}
{-# OPTIONS_GHC -Wno-overlapping-patterns #-}

import Data.Char (isAlpha, isDigit, isSpace, isUpper, isLetter)
import GHC.OldList (isPrefixOf)
import GHC.Unicode (toUpper)
import GHC.Base (Alternative(..))
import Data.Functor
import Data.Binary.Get (skip)
import Data.List
import Data.Maybe
import System.IO

newtype Parser a = Parser { parse :: String -> Maybe (String, a)}

instance Functor Parser where
    fmap :: forall a b. (a -> b) -> Parser a -> Parser b
    fmap func (Parser p1) = Parser p2 where
        p2 :: String -> Maybe (String, b)
        p2 input = case p1 input of
            Just (tail, val) -> Just (tail, func val)
            Nothing  -> Nothing

instance Applicative Parser where
    pure :: a -> Parser a
    pure x = Parser $ \s -> Just (s, x)
    (<*>) :: forall a b. Parser (a -> b) -> Parser a -> Parser b
    (Parser p1) <*> (Parser p2) = Parser p where
        p :: String -> Maybe (String, b)
        p input = case p1 input of
            Just (tail, val) -> case p2 tail of
                Just (tail2, val2) -> Just (tail2, val val2)
                Nothing -> Nothing
            Nothing -> Nothing

instance Alternative Parser where
    empty :: Parser a
    empty = Parser $ const Nothing
    (<|>) :: forall a. Parser a -> Parser a -> Parser a
    (Parser p1) <|> (Parser p2) = Parser p where
        p :: String -> Maybe (String, a)
        p input = case p1 input of
            Just (tail, val) -> Just (tail, val)
            Nothing -> p2 input

parseString :: String -> Parser a -> Maybe a
parseString str (Parser p) = case p str of
    Just ("", val) -> Just val
    Nothing        -> Nothing

predP :: (Char -> Bool) -> Parser Char
predP p = Parser f where
    f ""                 = Nothing
    f (c:cs) | p c       = Just (cs, c)
             | otherwise = Nothing

charP :: Char -> Parser Char
charP c = predP (== c)

stringP :: String -> Parser String
stringP pattern = Parser f where
    f input | pattern `isPrefixOf` input = Just (drop (length pattern) input, pattern)
            | otherwise                  = Nothing

starPredP :: (Char -> Bool) -> Parser String
starPredP pred = Parser p where
    p :: String -> Maybe (String, String)
    p input = let result = takeWhile pred input
        in Just (drop (length result) input, result)

andP :: forall a. Parser a -> Parser [a] -> Parser [a]
andP (Parser p1) (Parser p2) = Parser p where
    p :: String -> Maybe (String, [a])
    p input = case p1 input of
        Just (t1, s1) -> case p2 t1 of
            Just (t2, s2) -> Just (t2, s1 : s2)
            Nothing -> Nothing
        Nothing -> Nothing

skipWsP :: Parser ()
skipWsP = () <$ starPredP isSpace

data Expression
    = Implication Expression Expression
    | Disjunction Expression Expression
    | Conjuction Expression Expression
    | Negation Expression
    | Variable String
    deriving Eq

instance Show Expression where
    show :: Expression -> String
    show (Variable s) = s
    show (Negation s) = "!" ++ show s
    show (Conjuction x y) = show x ++ "&" ++ show y
    show (Disjunction x y) = show x ++ "|" ++ show y
    show (Implication x y) = show x ++ "->" ++ show y

implicationP :: Parser Expression
implicationP =
    skipWsP *>
        (
            (Implication <$>
                disjunctionP
                <* skipWsP <* charP '-' <* charP '>' <* skipWsP
                <*> implicationP
            ) <|> disjunctionP
        )

leftHandTreeP :: (Expression -> Expression -> Expression) -> Char -> Parser Expression -> Parser Expression
leftHandTreeP constructor constructor_symbol parser@(Parser p1) = Parser $ \s ->
    case p1 s of
        Just (tail, value) -> case parse (many $ skipWsP *> charP constructor_symbol *> skipWsP *> parser) tail of
            Just (tail2, values) -> Just (tail2, foldl constructor value values)
            Nothing -> Just (tail, value)
        Nothing -> Nothing


disjunctionP :: Parser Expression
disjunctionP = leftHandTreeP Disjunction '|' conjuctionP

conjuctionP :: Parser Expression
conjuctionP = leftHandTreeP Conjuction '&' negationP

negationP :: Parser Expression
negationP =
    skipWsP *> (
        (Negation <$> (charP '!' *> negationP))
        <|> variableP
        <|> (charP '(' *> skipWsP *>
                implicationP
                <* skipWsP <* charP ')'))

variableP :: Parser Expression
variableP = Variable <$> (skipWsP *> variableParser')
    where
        variableParser' :: Parser String
        variableParser' = andP headParser tailParser
        headParser :: Parser Char
        headParser = predP (\c -> isUpper c && isLetter c)
        tailParser :: Parser String
        tailParser = starPredP (\c -> isUpper c && isLetter c || isDigit c || c == '\39')

someDelimP :: Parser a -> Parser () -> Parser [a]
someDelimP contentP delimiterP =
    andP contentP $ many (delimiterP *> contentP)

manyDelimP :: Parser a -> Parser () -> Parser [a]
manyDelimP contentP delimiterP = someDelimP contentP delimiterP <|> pure []

type Context = [Expression]

contextP :: Parser Context
contextP = manyDelimP (skipWsP *> implicationP) (() <$ starPredP (\c -> isSpace c || c == ','))

data ProofStep = ProofStep Integer Context Expression

instance Show ProofStep where
    show :: ProofStep -> String
    show (ProofStep n context expr) = "[" ++ show n ++ "] " ++ (intercalate "," (map show context)) ++ "|-" ++ show expr

proofStepP :: Integer -> Parser ProofStep
proofStepP n = ProofStep n <$> contextP <*> (skipWsP *> stringP "|-" *> implicationP)

data ProofType
    = Axiom Integer
    | Hypothesis Integer
    | ModusPonens Integer Integer
    | DeductionTheorem Integer
    | Incorrect

instance Show ProofType where
    show :: ProofType -> String
    show (Axiom n) = "[Ax. sch. " ++ show n ++ "]"
    show (Hypothesis n) = "[Hyp. " ++ show n ++ "]"
    show (ModusPonens a b) = "[M.P. " ++ show a ++ ", " ++ show b ++ "]"
    show (DeductionTheorem n) = "[Ded. " ++ show n ++ "]"
    show Incorrect = "[Incorrect]"

printProof :: ProofStep -> ProofType -> String
printProof step proof = show step ++ " " ++ show proof

isAxiom1 :: Expression -> Bool
isAxiom1 (Implication a (Implication b a1)) = a == a1
isAxiom1 _ = False

isAxiom2 :: Expression -> Bool
isAxiom2 (Implication (Implication a b) (Implication (Implication a1 (Implication b1 c)) (Implication a2 c1))) =
    a == a1 && a == a2 && b == b1 && c == c1
isAxiom2 _ = False

isAxiom3 :: Expression -> Bool
isAxiom3 (Implication a (Implication b (Conjuction a1 b1))) = a == a1 && b == b1
isAxiom3 _ = False

isAxiom4 :: Expression -> Bool
isAxiom4 (Implication (Conjuction a b) a1) = a == a1
isAxiom4 _ = False

isAxiom5 :: Expression -> Bool
isAxiom5 (Implication (Conjuction a b) b1) = b == b1
isAxiom5 _ = False

isAxiom6 :: Expression -> Bool
isAxiom6 (Implication a (Disjunction a1 b)) = a == a1
isAxiom6 _ = False

isAxiom7 :: Expression -> Bool
isAxiom7 (Implication b (Disjunction a b1)) = b == b1
isAxiom7 _ = False

isAxiom8 :: Expression -> Bool
isAxiom8 (Implication (Implication a c) (Implication (Implication b c1) (Implication (Disjunction a1 b1) c2))) =
    a == a1 && b == b1 && c == c1 && c == c2
isAxiom8 _ = False

isAxiom9 :: Expression -> Bool
isAxiom9 (Implication (Implication a b) (Implication (Implication a1 (Negation b1)) (Negation a2))) =
    a == a1 && a == a2 && b == b1
isAxiom9 _ = False

isAxiom10 :: Expression -> Bool
isAxiom10 (Implication (Negation (Negation a)) a1) = a == a1
isAxiom10 _ = False

toAxiom :: ProofStep -> Maybe ProofType
toAxiom (ProofStep _ _ expr)
    | isAxiom1 expr  = Just $ Axiom 1
    | isAxiom2 expr  = Just $ Axiom 2
    | isAxiom3 expr  = Just $ Axiom 3
    | isAxiom4 expr  = Just $ Axiom 4
    | isAxiom5 expr  = Just $ Axiom 5
    | isAxiom6 expr  = Just $ Axiom 6
    | isAxiom7 expr  = Just $ Axiom 7
    | isAxiom8 expr  = Just $ Axiom 8
    | isAxiom9 expr  = Just $ Axiom 9
    | isAxiom10 expr = Just $ Axiom 10
    | otherwise      = Nothing

toHypothesis :: ProofStep -> Maybe ProofType
toHypothesis (ProofStep _ context expr) = case elemIndex expr context of
    Just n -> Just $ Hypothesis $ toInteger n + 1
    Nothing -> Nothing

areListsEqual :: (Eq a) => [a] -> [a] -> Bool
areListsEqual x y = null (x \\ y) && null (y \\ x)

-- HINT: Making assumption that MP 2 4 is correct
toModusPonens :: [ProofStep] -> ProofStep -> Maybe ProofType
toModusPonens prev cur = (\(ProofStep n1 g1 a1, ProofStep n2 g2 a2) -> ModusPonens n1 n2) <$> toModusPonens' prev prev cur
    where
        toModusPonens' :: [ProofStep] -> [ProofStep] -> ProofStep -> Maybe (ProofStep, ProofStep)
        toModusPonens' [] _ _ = Nothing
        toModusPonens' (sec:xs) fullPrev res
            | isSecondPartOfMP res sec = case find (isFirstPartOfMP sec) fullPrev of
                Nothing -> toModusPonens' xs fullPrev cur
                Just fir  -> Just (fir, sec)
            | otherwise  = toModusPonens' xs fullPrev cur
        isSecondPartOfMP :: ProofStep -> ProofStep -> Bool
        isSecondPartOfMP (ProofStep _ g b) (ProofStep _ g1 (Implication _ b1)) = b == b1 && areListsEqual g g1
        isSecondPartOfMP _ _ = False
        isFirstPartOfMP :: ProofStep -> ProofStep -> Bool
        isFirstPartOfMP (ProofStep _ g (Implication a _)) (ProofStep _ g1 a1) = a == a1 && areListsEqual g g1
        isFirstPartOfMP _ _ = False

toDeductionTheorem :: [ProofStep] -> ProofStep -> Maybe ProofType
toDeductionTheorem prev cur = (\(ProofStep n _ _) -> DeductionTheorem n) <$> find (areEquiv cur) prev
    where
        moveImplication :: Context -> Expression -> (Context, Expression)
        moveImplication context (Implication a b) = moveImplication (a:context) b
        moveImplication context a = (context, a)
        areEquiv :: ProofStep -> ProofStep -> Bool
        areEquiv (ProofStep _ g1 a1) (ProofStep _ g2 a2) =
            let (g1', a1') = moveImplication g1 a1
                (g2', a2') = moveImplication g2 a2
            in (areListsEqual g1' g2') && (a1' == a2')


getProof :: [ProofStep] -> ProofStep -> ProofType
getProof prev cur = (\(Just x) -> x) (toAxiom cur <|> toHypothesis cur <|> toModusPonens prev cur <|> toDeductionTheorem prev cur <|> Just Incorrect)

main :: IO ()
main = myLoop [] 1

myLoop :: [ProofStep] -> Integer -> IO ()
myLoop prev n = do
    done <- isEOF
    if done then return () else do
        s <- getLine 
        let Just p = parseString s (proofStepP n)
            proof = getProof prev p 
        putStrLn $ "[" ++ show n ++ "] " ++ s ++ " " ++ show proof
        myLoop (p:prev) (n + 1)
