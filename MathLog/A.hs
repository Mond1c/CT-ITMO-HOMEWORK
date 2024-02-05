{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use void" #-}
{-# HLINT ignore "Use second" #-}
{-# LANGUAGE ScopedTypeVariables, InstanceSigs #-}

import Data.Char (isAlpha, isDigit, isSpace, isUpper, isLetter)
import GHC.OldList (isPrefixOf)
import GHC.Unicode (toUpper)
import GHC.Base (Alternative(..))
import Data.Functor
import Data.Binary.Get (skip)

newtype Parser a = Parser { parse :: String -> (String, Maybe a)}

instance Functor Parser where
    fmap :: forall a b. (a -> b) -> Parser a -> Parser b
    fmap func (Parser p1) = Parser p2 where
        p2 :: String -> (String, Maybe b)
        p2 input = case p1 input of
            (tail, Just val) -> (tail, Just $ func val)
            (tail, Nothing)  -> (tail, Nothing)

instance Applicative Parser where
    pure :: a -> Parser a
    pure x = Parser (, Just x)
    (<*>) :: forall a b. Parser (a -> b) -> Parser a -> Parser b
    (Parser p1) <*> (Parser p2) = Parser p where
        p :: String -> (String, Maybe b)
        p input = case p1 input of
            (tail, Just val) -> case p2 input of
                (tail2, Just val2) -> (tail2, Just (val val2))
                (tail2, Nothing) -> (tail2, Nothing)
            (tail, Nothing) -> (tail, Nothing)

instance Alternative Parser where
    empty :: Parser a
    empty = Parser (const ("", Nothing))
    (<|>) :: forall a. Parser a -> Parser a -> Parser a
    (Parser p1) <|> (Parser p2) = Parser p where
        p :: String -> (String, Maybe a)
        p input = case p1 input of
            (tail, Just val) -> (tail, Just val)
            (tail, Nothing) -> p2 input

parseString :: String -> Parser a -> Maybe a
parseString str (Parser p) = case p str of
    ("", Just val) -> Just val
    _              -> Nothing

predParser :: (Char -> Bool) -> Parser Char
predParser p = Parser f where
    f ""                 = ("", Nothing)
    f (c:cs) | p c       = (cs, Just c)
             | otherwise = (c:cs, Nothing)

charParser :: Char -> Parser Char
charParser c = predParser (== c)

stringParser :: String -> Parser String
stringParser pattern = Parser f where
    f input | pattern `isPrefixOf` input = (drop (length pattern) input, Just pattern)
            | otherwise                  = (input, Nothing)

starPredParser :: (Char -> Bool) -> Parser String
starPredParser pred = Parser p where
    p :: String -> (String, Maybe String)
    p input = let result = dropWhile pred input
        in (result, Just $ take (length input - length result) input)

andParser :: Parser String -> Parser String -> Parser String
andParser (Parser p1) (Parser p2) = Parser p where
    p :: String -> (String, Maybe String)
    p input = case p1 input of
        (t1, Just s1) -> case p2 t1 of
            (t2, Just s2) -> (t2, Just $ s1 ++ s2)
            (_, Nothing) -> (t1, Nothing)
        (t1, Nothing) -> (t1, Nothing)

skipWSParser:: Parser ()
skipWSParser = () <$ starPredParser isSpace

data Expression
    = Implication Expression Expression
    | Disjunction Expression Expression
    | Conjuction Expression Expression
    | Negation Expression
    | Variable String

instance Show Expression where
    show :: Expression -> String
    show (Variable s) = s
    show (Negation s) = "(!" ++ show s ++ ")"
    show (Conjuction x y) = "(&," ++ show x ++ "," ++ show y ++ ")"
    show (Disjunction x y) = "(|," ++ show x ++ "," ++ show y ++ ")"
    show (Implication x y) = "(->," ++ show x ++ "," ++ show y ++ ")"

-- implicationParser :: Parser Expression
-- implicationParser = 
--     skipWhitespacesParser *>
--         (
--             (Implication <$> 
--                 disjunctionParser 
--                 <* skipWhitespacesParser <* charParser '-' <* charParser '>' <* skipWhitespacesParser
--                 <*> implicationParser
--             ) <|> disjunctionParser 
--         )


-- leftHandTreeParser :: (Expression -> Expression -> Expression) -> Char -> Parser Expression -> Parser Expression
-- leftHandTreeParser constructor constructor_symbol parser = Parser $ \s ->
--     let [(tail, value)] = parse (skipWhitespacesParser *> parser) s 
--         parse_part = many $ skipWhitespacesParser *> charParser constructor_symbol *> skipWhitespacesParser *> parser
--         result = parse parse_part tail
--     in case result of 
--         []                      -> [(tail, value)]
--         ((tail, values) : xs)   -> [(tail, foldl constructor value values)]


-- disjunctionParser :: Parser Expression
-- disjunctionParser = leftHandTreeParser Disjunction '|' conjuctionParser

-- conjuctionParser :: Parser Expression
-- conjuctionParser = leftHandTreeParser Conjuction '&' negationParser

-- negationParser :: Parser Expression
-- negationParser = 
--     skipWhitespacesParser *> (
--         (Negation <$> (charParser '!' *> negationParser))  
--         <|> variableParser
--         <|> (charParser '(' *> skipWhitespacesParser *>
--                 implicationParser
--                 <* skipWhitespacesParser <* charParser ')'))
 
variableParser :: Parser Expression
variableParser = Variable <$> (skipWSParser *> variableParser')
    where
        variableParser' :: Parser String
        variableParser' = andParser ((: []) <$> headParser) tailParser
        headParser :: Parser Char
        headParser = predParser (\c -> isUpper c && isLetter c)
        tailParser :: Parser String
        tailParser = starPredParser (\c -> isUpper c && isLetter c || isDigit c || c == '\39')

parser1 :: Parser (String -> String)
parser1 = Parser p where
    p :: String -> (String, Maybe (String -> String))
    p input = let result = parse input $ starPredParser isDigit
        in result

parser2 :: Parser String
parser2 = starPredParser (\c -> c == 'A')

main :: IO ()
main = do
    let s = "123123AAAAA  BDFDFDFD23232'BCBDaaaaa"
        variableParser' :: Parser String
        variableParser' = skipWSParser *> andParser headParser' tailParser
        headParser' :: Parser String
        headParser' = (: []) <$> headParser
        headParser :: Parser Char
        headParser = predParser (\c -> isUpper c && isLetter c)
        tailParser :: Parser String
        tailParser = starPredParser (\c -> isUpper c && isLetter c || isDigit c || c == '\39')
    print $ parse parser2 s