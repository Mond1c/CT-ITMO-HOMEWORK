"use strict";

function Const(value) { this.value = parseFloat(value) }

const ZERO = new Const(0)
const ONE = new Const(1)

Const.prototype.toString = function () { return this.value.toString() }
Const.prototype.prefix = function () { return this.value.toString() }
Const.prototype.postfix = function () { return this.value.toString() }
Const.prototype.evaluate = function () { return this.value }
Const.prototype.diff = function () { return ZERO }

function Variable(value) { this.value = value }

const fromStringToVariable = ['x', 'y', 'z']

Variable.prototype.toString = function () { return this.value }
Variable.prototype.prefix = function () { return this.value }
Variable.prototype.postfix = function () { return this.value }
Variable.prototype.evaluate = function (...args) { return args[fromStringToVariable.indexOf(this.value)] }
Variable.prototype.diff = function (name) { return this.value === name ? ONE : ZERO }


const BaseOperation = {
    toString: function () { return this.children.join(' ') + ' ' + this.stringRepresentation; },
    prefix: function () {
        let result = '(' + this.stringRepresentation + ' ';
        for (let index = 0; index < this.children.length; index++) {
            result += this.children[index].prefix() + (index === this.children.length - 1 ? '' : ' ')
        }
        result += ')'
        return result
    },
    postfix: function () {
        let result = '(';
        for (let index = 0; index < this.children.length; index++) {
            result += this.children[index].postfix() + (index === this.children.length - 1 ? '' : ' ')
        }
        result += ' ' + this.stringRepresentation + ')'
        return result
    },
    evaluate: function (x, y, z) { return this.functor(...this.children.map(child => child.evaluate(x, y, z))) },
    diff: function (name) { return this.diffFunctor(name, ...this.children) },
}

let fromStringToFunction = new Map()
let countOfArgs = new Map()

function createOperation(functor, diffFunctor, stringRepresentation, countOfArguments) {
    let result = function (...children) { this.children = children }
    result.prototype = Object.create(BaseOperation)
    result.prototype.functor = functor
    result.prototype.diffFunctor = diffFunctor
    result.prototype.stringRepresentation = stringRepresentation

    fromStringToFunction.set(stringRepresentation, result)
    countOfArgs.set(result, countOfArguments)

    return result
}

const Add = createOperation(
    (a, b) => a + b,
    (name, a, b) => new Add(a.diff(name), b.diff(name)),
    '+', 2)
const Subtract = createOperation(
    (a, b) => a - b,
    (name, a, b) => new Subtract(a.diff(name), b.diff(name)),
    '-', 2)
const Multiply = createOperation(
    (a, b) => a * b,
    (name, a, b) => new Add(new Multiply(a, b.diff(name)), new Multiply(a.diff(name), b)),
    '*', 2)
const Divide = createOperation(
    (a, b) => a / b,
    (name, a, b) => new Divide(new Subtract(new Multiply(a.diff(name), b), new Multiply(b.diff(name), a)), new Multiply(b, b)),
    '/', 2)
const Negate = createOperation(
    a => -a,
    (name, a) => new Negate(a.diff(name)),
    'negate', 1)
const Square = createOperation(
    a => a * a,
    (name, a) => new Multiply(new Multiply(new Const(2), a), a.diff(name)),
    'sqr', 1)
const SquareRoot = createOperation(
    a => Math.sqrt(a),
    (name, a) => new Divide(a.diff(name), new Multiply(new Const(2), new SquareRoot(a))),
    'sqrt', 1)

function SumsqAsElementaryFunctions(children) {
    return children.map(child => new Square(child)).reduce((accumulator, current) => new Add(accumulator, current))
}

function SumsqEvaluate(children) {
    return children.reduce((accumulator, current) => accumulator + current ** 2, 0);
}

function createSumsqN(N) {
    return createOperation(
        (...children) => SumsqEvaluate(children),
        (name, ...children) => SumsqAsElementaryFunctions(children).diff(name),
        'sumsq'.concat(N.toString()),
        N
    )
}

function createDistance(N) {
    return createOperation(
        (...children) => SumsqEvaluate(children) ** 0.5,
        (name, ...children) => new SquareRoot(SumsqAsElementaryFunctions(children)).diff(name),
        'distance'.concat(N.toString()),
        N
    )
}

let Sumsq2 = createSumsqN(2)
let Sumsq3 = createSumsqN(3)
let Sumsq4 = createSumsqN(4)
let Sumsq5 = createSumsqN(5)

let Distance2 = createDistance(2)
let Distance3 = createDistance(3)
let Distance4 = createDistance(4)
let Distance5 = createDistance(5)

function parse(stringExpression) {
    let tokens = stringExpression.split(' ').filter(x => x.length !== 0)
    let operands = []
    for (let token of tokens) {
        if (fromStringToFunction.has(token)) {
            let operator = fromStringToFunction.get(token)
            let countOfArguments = countOfArgs.get(operator)
            operands.push(new operator(...operands.splice(-countOfArguments, countOfArguments)))
        } else if (fromStringToVariable.includes(token)) {
            operands.push(new Variable(token))
        } else {
            operands.push(new Const(token))
        }
    }
    return operands[0]
}

// =============HW 8=================

let Exponent = createOperation(
    a => Math.E ** a,
    (name, a) => new Multiply(new Exponent(a), a.diff(name)),
    'exp',
    1
)

let Log = createOperation(
    a => Math.log(a),
    (name, a) => new Divide(a.diff(name), a),
    'ln',
    1
)

function SumexpAsElementary(children) {
    return children.reduce((accumulator, current) => new Add(accumulator, new Exponent(current)), ZERO);
}

function SumexpEvaluate(children) {
    return children.reduce((accumulator, current) => accumulator + Math.E ** current, 0);
}

let Sumexp = createOperation(
    (...children) => SumexpEvaluate(children),
    (name, ...children) => SumexpAsElementary(children).diff(name),
    'sumexp',
    Infinity
)

let LSE = createOperation(
    (...children) => Math.log(SumexpEvaluate(children)),
    (name, ...children) => new Log(SumexpAsElementary(children)).diff(name),
    'lse',
    Infinity
)

class ParserError extends Error {
    constructor(message, idx) {
        if (idx === undefined) {
            super('Parsing error: ' + message)
        } else {
            super('Parsing error: ' + message + " at pos " + idx)
        }
    }
    static name = "ParserError"
}

function* getNextToken(stringExpression) {
    const allOccurences = [...stringExpression.matchAll(/\(|\)|[^\r\n\t\f\v\)\( )]+/g)]
    // splitting stringExpression to '(', ')', and tokens
    for (let occurence of allOccurences) {
        yield { str: occurence[0], index: occurence.index }
    }
}

class PeekableIterator {
    constructor(iterator) {
        this.iterator = iterator
        this.value = this.iterator.next()
    }

    next() {
        this.value = this.iterator.next()
        return this.value
    }

    peek() {
        return this.value
    }
}

class Parser {
    constructor(stringExpression) {
        this.iterator = new PeekableIterator(getNextToken(stringExpression))
    }

    parseElementary() {
        let curToken = this.iterator.peek()
        let tokenStr = curToken.value.str
        if (fromStringToVariable.includes(tokenStr)) {
            return new Variable(tokenStr)
        } else if (!isNaN(tokenStr)) {
            return new Const(tokenStr)
        } else if (fromStringToFunction.has(tokenStr)) {
            return fromStringToFunction.get(tokenStr)
        } else {
            throw new ParserError("Unknown token: '" + tokenStr + "'", curToken.value.index)
        }
    }

    parseArgument() {
        let curToken = this.iterator.peek()
        if (curToken.done) { // if there's nothing
            throw new ParserError("Expected arguments, found end")
        }
        if (curToken.value.str !== '(') {
            return this.parseElementary();
        }

        let startOfBracketSequence = curToken.value.index
        let stack = []
        let operators = []
        curToken = this.iterator.next()
        while (curToken.done !== true && curToken.value.str !== ')') {
            let currentLocation = curToken.value.index
            let argument = this.parseArgument()
            if (argument instanceof Function) {
                operators.push({ locationArgs: stack.length, locationStr: currentLocation })
            }
            stack.push(argument)
            curToken = this.iterator.next()
        }

        if (curToken.done === true) { // if sequence hasn't ended by ')'
            throw new ParserError("Expected ')', found end; in bracket-sequence started", startOfBracketSequence)
        }

        if (stack.length === 0) {
            throw new ParserError("Found forbidden sequence '()'", startOfBracketSequence)
        }

        if (operators.length === 0) {
            throw new ParserError(`There's no operator (form requires operator to be at ${this.parsingPrefix ? "first" : "last"} position) in bracket-sequence`, startOfBracketSequence)
        } else if (operators.length > 1) {
            throw new ParserError(`There're too many operators (${operators.map(el => `${stack[el.locationArgs].prototype.stringRepresentation} at pos ${el.locationStr}`).join('; ')}) in bracket-sequence started`, startOfBracketSequence)
        } else if (operators[0].locationArgs !== (this.parsingPrefix ? 0 : stack.length - 1)) {
            throw new ParserError(`Found operator ${stack[operators[0].locationArgs].prototype.stringRepresentation} (at pos ${operators[0].locationStr}), but form requires operator to be at ${this.parsingPrefix ? "first" : "last"} position; in bracket-sequence`, startOfBracketSequence)
        }

        let operator = stack[operators[0].locationArgs]
        let countOfOperatorArguments = countOfArgs.get(operator)

        if (stack.length - 1 !== countOfOperatorArguments && countOfOperatorArguments !== Infinity) {
            throw new ParserError(`Expected ${countOfOperatorArguments} arguments from operator ${operator.prototype.stringRepresentation} (at pos ${operators[0].locationStr}), found ${stack.length - 1} arguments from bracket - sequence started`, startOfBracketSequence)
        }

        stack.splice(operators[0].locationArgs, 1)
        return new operator(...stack)
    }

    parse(isPrefix) {
        this.parsingPrefix = isPrefix
        let result = this.parseArgument()
        if (this.iterator.next().done !== true) {
            throw new ParserError(`Expected end, found '${this.iterator.peek().value.str}'`, this.iterator.peek().value.index)
        }
        return result
    }
}

function parsePrefix(stringExpression) {
    return new Parser(stringExpression).parse(true)
}

function parsePostfix(stringExpression) {
    return new Parser(stringExpression).parse(false)
}
