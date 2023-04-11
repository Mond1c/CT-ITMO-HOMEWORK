"use strict"

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

// :NOTE: "abstract" class
function newPartOfExpression(part, evaluate, diff, toString, prefix = toString, postfix = toString) {
    part.prototype.evaluate = evaluate;
    part.prototype.diff = diff;
    part.prototype.toString = toString;
    part.prototype.prefix = prefix;
    part.prototype.postfix = postfix;
}

function Const(x) {
    this.x = Number(x);
}

const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

newPartOfExpression(
    Const,
    function () {
        return this.x;
    },
    function () {
        return ZERO; // :NOTE: new
    },
    function () {
        return this.x.toString();
    }
)

function Variable(varName) {
    this.varName = varName;
}

newPartOfExpression(
    Variable,
    function (...args) {
        return args[variableToIndex.get(this.varName)];
    },
    function (varName) {
        return varName === this.varName ? ONE : ZERO;
    },
    function () {
        return this.varName;
    }
);

function Operation(...parts) {
    this.parts = parts;
}

newPartOfExpression(
    Operation,
    function (...args) {
        return this.calculate(...this.parts.map(part => part.evaluate(...args)));
    },
    function (varName) {
        return this.diffOperation(varName, ...this.parts);
    },
    function () {
        return this.parts.map(part => part.toString()).join(' ') + " " + this.operationName;
    },
    function () {
        return '(' + this.operationName + ' ' + this.parts.map(part => part.prefix()).join(' ') + ')';
    },
    function () {
        return '(' + this.parts.map(part => part.postfix()).join(' ') + ' ' + this.operationName + ')';
    }
);

function newNOperation(n, operationName, calculate, diffOperation) {
    const obj = function (...parts) {
        Operation.call(this, ...parts);
    }
    obj.argsCount = n;
    obj.prototype = Object.create(Operation.prototype);
    obj.prototype.operationName = operationName;
    obj.prototype.calculate = calculate;
    obj.prototype.diffOperation = diffOperation;
    return obj;
}

function newOperation(operationName, calculate, diffOperation) {
    return newNOperation(calculate.length, operationName, calculate, diffOperation);
}

// const add = operation("+", (a, b) => a + b, (v, l, r) => new Add(l.diff(v), r.diff(v)), 2);

const Add = newOperation('+', (x, y) => x + y,
    (varName, l, r) => new Add(l.diff(varName), r.diff(varName)));

const Subtract = newOperation('-', (x, y) => x - y,
    (varName, l, r) => new Subtract(l.diff(varName), r.diff(varName)));

const Multiply = newOperation('*', (x, y) => x * y,
    (varName, l, r) => new Add(
        new Multiply(l.diff(varName), r),
        new Multiply(l, r.diff(varName))
    ));

const Divide = newOperation('/', (x, y) => x / y,
    (varName, l, r) => new Divide(
        new Subtract(
            new Multiply(l.diff(varName), r),
            new Multiply(l, r.diff(varName))
        ),
        new Multiply(r, r)
    ));

const Negate = newOperation('negate', (x) => -x,
    (varName, part) => new Negate(part.diff(varName)));


const calculateSumsqn = (...args) => args.reduce((ans, x) => ans + x * x, 0);

// :NOTE: копипаста между SumsqN и DistanceN
// args.reduce((ans, x) => ans + x * x, 0
// parts.reduce((ans, part) => new Add(ans, new Multiply(part, part).diff(varName)), ZERO)
const SumsqN = (n) => newNOperation(n, 'sumsq' + n, calculateSumsqn,
    (varName, ...parts) => parts.reduce((ans, part) => new Add(ans, new Multiply(part, part).diff(varName)), ZERO))

const DistanceN = (n) => newNOperation(n, 'distance' + n, (...args) => Math.sqrt(calculateSumsqn(...args)),
    function (varName, ...parts) {
        return new Divide(parts.reduce((ans, part) => new Add(ans, new Multiply(part, part).diff(varName)), ZERO), new Multiply(TWO, this));
    });

const Sumsq2 = SumsqN(2);
const Sumsq3 = SumsqN(3);
const Sumsq4 = SumsqN(4);
const Sumsq5 = SumsqN(5);

const Distance2 = DistanceN(2);
const Distance3 = DistanceN(3);
const Distance4 = DistanceN(4);
const Distance5 = DistanceN(5);

const calculateSumexp = (...args) => args.reduce((ans, x) => ans + Math.exp(x), 0); 

// :NOTE: копипаста между Sumexp и LSE
// args.reduce((ans, x) => ans + Math.exp(x), 0)
const Sumexp = newOperation("sumexp", calculateSumexp,
    (varName, ...parts) => parts.reduce((ans, part) => new Add(ans, new Multiply(new Sumexp(part), part.diff(varName))), ZERO));

const LSE = newOperation("lse", (...args) => Math.log(calculateSumexp(...args)),
    // :NOTE: два раза создается один и тот же new Sumexp(...parts)
    function (varName, ...parts) {
        const sumexp = new Sumexp(...parts);
        return new Divide(sumexp.diff(varName), sumexp);
    });


const tokenToOperation = new Map([
    ["+", Add],
    ["-", Subtract],
    ["*", Multiply],
    ["/", Divide],
    ["negate", Negate],
    ["sumsq2", Sumsq2],
    ["sumsq3", Sumsq3],
    ["sumsq4", Sumsq4],
    ["sumsq5", Sumsq5],
    ["distance2", Distance2],
    ["distance3", Distance3],
    ["distance4", Distance4],
    ["distance5", Distance5],
    ["sumexp", Sumexp],
    ["lse", LSE]
]);

function ParserError(message) {
    this.message = message;
}
ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.constructor = ParserError;
ParserError.prototype.name = "ParserError";

function newParserError(errorName, message) {
    const parserError = function(...args) {
        ParserError.call(this, message(...args));
    }
    parserError.prototype = Object.create(ParserError.prototype);
    parserError.errorName = errorName;
    parserError.constructor = parserError;
    return parserError;
}

const UnsupportedOperation = newParserError("UnsupportedOperation", (token, pos) => "Unsupported operation: " + token + " at position " + pos);
const IllegalCountOfArgs = newParserError("IllegalCountOfArgs", (token, count1, count2, pos) => "Illegal count of arguments in " + token + ": " + count1 + " != " + count2 + " at position " + pos);
const UnsupportedToken = newParserError("UnsupportedToken", (token, pos) => "Unsupported Token " + token + " at position " + pos);
const MissingBracket = newParserError("MissingBracket", (pos) => 'Missing closing bracket for ( at position ' + pos);
const EmptyExpressionError = newParserError("EmptyExpressionError", () => "Input expression is empty");
const UnexpectedEndToken = newParserError("UnexpectedEndToken", (token, pos) => "Unexpected token: " + token + " at the end of the expression (at position " + pos + ")");


function parseOperation(pos, operation, ...args) {
    if (!tokenToOperation.has(operation)) {
        throw new UnsupportedOperation(operation, pos - operation.length);
    }
    const op = tokenToOperation.get(operation);
    if (op.argsCount !== 0 && op.argsCount !== args.length) {
        throw new IllegalCountOfArgs(operation, op.argsCount, args.length, pos);
    }
    return new op(...args);
}

function parseVariableAndConst(token, position = 0) {
    if (variableToIndex.has(token)) {
        return new Variable(token);
    }
    if (isNaN(Number(token))) {
        throw new UnsupportedToken(token, position - token.length);
    }
    return new Const(token);
}

function parse(expression) {
    return expression.split(/\s+/).filter(str => str !== "").reduce(
        (stack, token) => {
            if (tokenToOperation.has(token)) {
                const operation = tokenToOperation.get(token);
                stack.push(new operation(...stack.splice(-operation.argsCount)));
            } else {
                stack.push(parseVariableAndConst(token));
            }
            return stack;
        }, []
    ).pop();
}

class StringSource {
    #source;
    #pos;

    constructor(source) {
        this.#source = source;
        this.#pos = 0;
    }

    #prev(n) {
        this.#pos -= n;
    }


    #isSeparator() {
        return this.#source[this.#pos] === ' ' || this.#source[this.#pos] === '(' || this.#source[this.#pos] === ')';
    }

    #skipWhitespaces() {
        while (this.#pos < this.#source.length && this.#source[this.#pos] === ' ') {
            this.#pos++;
        }
    }

    getToken() {
        this.#skipWhitespaces();
        if (this.#isSeparator()) {
            return this.#source[this.#pos++];
        }
        let ans = '';
        while (this.#pos < this.#source.length && !this.#isSeparator()) {
            ans += this.#source[this.#pos++];
        }
        return ans;
    }

    getTestToken() {
        let cur = this.getToken();
        this.#prev(cur.length);
        return cur;
    }

    hasNext() {
        return this.#pos + 1 <= this.#source.length;
    }

    getPos() {
        return this.#pos;
    }
}

class Parser {
    #mode;
    #source;

    constructor(mode) {
        this.#mode = mode;
    }

    #parseOperation() {
        let token;
        if (this.#mode === 'prefix') {
            token = this.#source.getToken();
        }
        let args = [];
        while (this.#source.hasNext() && this.#source.getTestToken() !== ')' && !tokenToOperation.has(this.#source.getTestToken())) {
            args.push(this.#parseToken(this.#source.getToken()));
        }
        if (this.#mode === "postfix") {
            token = this.#source.getToken();
        }
        return parseOperation(this.#source.getPos(), token, ...args);
    }

    #parseToken(token) {
        if (token === '(') {
            const operation = this.#parseOperation();
            let nextToken = this.#source.getToken();
            if (nextToken !== ')') {
                throw new MissingBracket(this.#source.getPos() - nextToken.length);
            }
            return operation;
        } else {
            return parseVariableAndConst(token, this.#source.getPos());
        }
    }

    parse(expression) {
        expression = expression.trim();
        if (expression === '') {
            throw new EmptyExpressionError();
        }
        this.#source = new StringSource(expression);
        const ans = this.#parseToken(this.#source.getToken());
        if (this.#source.hasNext()) {
            throw new UnexpectedEndToken(this.#source.getToken(), this.#source.getPos() - 1);
        }
        return ans;
    }
}

const PrefixParser = new Parser("prefix");
const PostfixParser = new Parser("postfix");

const parsePrefix = (expression) => PrefixParser.parse(expression);

const parsePostfix = (expression) => PostfixParser.parse(expression);

// :NOTE: ошибки выбрасываются без сообщений -- см вывод тестов
// Empty input              : org.graalvm.polyglot.PolyglotException: Error
// Unknown variable         : org.graalvm.polyglot.PolyglotException: Error
// Invalid number           : org.graalvm.polyglot.PolyglotException: Error
// Missing )                : org.graalvm.polyglot.PolyglotException: Error
// Missing (                : org.graalvm.polyglot.PolyglotException: Error
// Unknown operation        : org.graalvm.polyglot.PolyglotException: Error
// Excessive info           : org.graalvm.polyglot.PolyglotException: Error
// Empty op                 : org.graalvm.polyglot.PolyglotException: Error
// Invalid unary (0 args)   : org.graalvm.polyglot.PolyglotException: Error
// Invalid unary (2 args)   : org.graalvm.polyglot.PolyglotException: Error
// Invalid binary (0 args)  : org.graalvm.polyglot.PolyglotException: Error
// Invalid binary (1 args)  : org.graalvm.polyglot.PolyglotException: Error
// Invalid binary (3 args)  : org.graalvm.polyglot.PolyglotException: Error
// Variable op (0 args)     : org.graalvm.polyglot.PolyglotException: Error
// Variable op (1 args)     : org.graalvm.polyglot.PolyglotException: Error
// Variable op (2 args)     : org.graalvm.polyglot.PolyglotException: Error
// Const op (0 args)        : org.graalvm.polyglot.PolyglotException: Error
// Const op (1 args)        : org.graalvm.polyglot.PolyglotException: Error
// Const op (2 args)        : org.graalvm.polyglot.PolyglotException: Error


// :NOTE: судя по коду, по ошибке нельзя понять в каком месте выражения произошла проблема
// в случае с выражениями на 100+ символов с одинаковыми токенами пользователь будет страдать