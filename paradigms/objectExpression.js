"use strict"

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

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
        return ZERO;
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

const SumsqN = (n) => newNOperation(n, 'sumsq' + n, (...args) => args.reduce((ans, x) => ans + x * x, 0),
    (varName, ...parts) => parts.reduce((ans, part) => new Add(ans, new Multiply(part, part).diff(varName)), ZERO))

const DistanceN = (n) => newNOperation(n, 'distance' + n, (...args) => Math.sqrt(args.reduce((ans, x) => ans + x * x, 0)),
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


const Sumexp = newOperation("sumexp", (...args) => args.reduce((ans, x) => ans + Math.exp(x), 0),
    (varName, ...parts) => parts.reduce((ans, part) => new Add(ans, new Multiply(new Sumexp(part), part.diff(varName))), ZERO));

const LSE = newOperation("lse", (...args) => Math.log(args.reduce((ans, x) => ans + Math.exp(x), 0)),
    (varName, ...parts) => new Divide(new Sumexp(...parts).diff(varName), new Sumexp(...parts)));


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

class UnsupportedOperation extends Error {
    constructor(token) {
        super();
        this.message = "Unsupported operation: " + token;
    }
}

class IllegalCountOfArgs extends Error {
    constructor(token, count1, count2) {
        super();
        this.message = "Illegal count of arguments in " + token + ": " + count1 + " != " + count2;
    }
}

class UnsupportedToken extends Error {
    constructor(token) {
        super();
        this.message = "Unsupported token: " + token;
    }
}

class MissingBracket extends Error {
    constructor(openPos) {
        super();
        this.message = 'Missing closing bracket for ( at position ' + openPos;
    }
}

class EmptyExpressionError extends Error {
    constructor() {
        super();
        this.messagee = "Input expression is empty";
    }

}

class UnexpectedEndToken extends Error {
    constructor(token) {
        super();
        this.message = "Unexpected token: " + token + " at the end of the expression";
    }
}

function parseOperation(operation, ...args) {
    if (!tokenToOperation.has(operation)) {
        throw new UnsupportedOperation(operation);
    }
    const op = tokenToOperation.get(operation);
    if (op.argsCount !== 0 && op.argsCount !== args.length) {
        throw new IllegalCountOfArgs(operation, op.argsCount, args.length);
    }
    return new op(...args);
}

function parseVariableAndConst(token) {
    if (variableToIndex.has(token)) {
        return new Variable(token);
    }
    if (isNaN(Number(token))) {
        throw new UnsupportedToken(token);
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


    isSeparator() {
        return this.#source[this.#pos] === ' ' || this.#source[this.#pos] === '(' || this.#source[this.#pos] === ')';
    }

    #skipWhitespaces() {
        while (this.#pos < this.#source.length && this.#source[this.#pos] === ' ') {
            this.#pos++;
        }
    }

    getToken() {
        this.#skipWhitespaces();
        if (this.isSeparator()) {
            return this.#source[this.#pos++];
        }
        let ans = '';
        while (this.#pos < this.#source.length && !this.isSeparator()) {
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

    parseOperation() {
        let token;
        if (this.#mode === 'prefix') {
            token = this.#source.getToken();
        }
        let args = [];
        while (this.#source.hasNext() && this.#source.getTestToken() !== ')' && !tokenToOperation.has(this.#source.getTestToken())) {
            args.push(this.parseToken(this.#source.getToken()));
        }
        if (this.#mode === "postfix") {
            token = this.#source.getToken();
        }
        return parseOperation(token, ...args);
    }

    parseToken(token) {
        if (token === '(') {
            const operation = this.parseOperation();
            let nextToken = this.#source.getToken();
            if (nextToken !== ')') {
                throw new MissingBracket(this.#source.getPos() - nextToken.length);
            }
            return operation;
        } else {
            return parseVariableAndConst(token);
        }
    }

    parse(expression) {
        expression = expression.trim();
        if (expression === '') {
            throw new EmptyExpressionError();
        }
        this.#source = new StringSource(expression);
        const ans = this.parseToken(this.#source.getToken());
        if (this.#source.hasNext()) {
            throw new UnexpectedEndToken(this.#source.getToken());
        }
        return ans;
    }
}

const PrefixParser = new Parser("prefix");
const PostfixParser = new Parser("postfix");

const parsePrefix = (expression) => PrefixParser.parse(expression);

const parsePostfix = (expression) => PostfixParser.parse(expression);
