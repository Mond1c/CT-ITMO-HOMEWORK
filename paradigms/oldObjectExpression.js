"use strict";

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

// :NOTE: "abstract" class
function PartOfExpression(obj, evaluate, diff, toString) {
    obj.prototype.evaluate = evaluate;
    obj.prototype.diff = diff;
    obj.prototype.toString = toString;
}

function Const(x) {
    this.x = x;
}


PartOfExpression(
    Const,
    function() {
        return Number(this.x);
    },
    function() {
        return new Const(0);
    },
    function() {
        return this.x + '';
    }
);

function Variable(varName) {
    this.varName = varName;
}

PartOfExpression(
    Variable,
    function(...args) {
        return args[variableToIndex.get(this.varName)];
    },
    function(varName) {
        return this.varName === varName ? new Const(1) : new Const(0);
    },
    function() {
        return this.varName;
    }
);

function Operation(...parts) {
    this.parts = parts;
}

PartOfExpression(
    Operation,
    function(...args) {
        return this.calculate(...this.parts.map(part => part.evaluate(...args)));
    },
    function(varName) {
        return this.diffOperation(varName, ...this.parts);
    },
    function() {
        return this.parts.map(part => part.toString()).join(" ") + " " + this.operationName;
    }
);

function NewOperation(operation, operationName, calculate, diffOperation) {
    operation.prototype = Object.create(Operation.prototype);
    operation.prototype.operationName = operationName;
    operation.prototype.calculate = calculate;
    operation.prototype.diffOperation = diffOperation;
}

function Add(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Add,
    "+",
    (...args) => args.reduce((ans, x) => ans += x, 0),
    (varName, ...args) => new Add(...args.map((arg) => arg.diff(varName)))
);

function Subtract(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Subtract,
    "-",
    (x, y) => x - y,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName))
);

function Multiply(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Multiply,
    "*",
    (x, y) => x * y,
    (varName, left, right) => new Add(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        )
);

function Divide(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Divide,
    "/",
    (x, y) => x / y,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        ),
        new Multiply(right, right)
    )
);


function Negate(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Negate,
    "negate",
    (x) => -x,
    (varName, left) => new Negate(left.diff(varName))
);

function SumsqN(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    SumsqN,
    "sumsq",
    (...args) => args.reduce((ans, x) => ans += x * x, 0),
    (varName, ...args) => new Add(...args.map((arg) => new Multiply(arg, arg).diff(varName)))
);

function NewSumsqN(sumsq, operationName) {
    sumsq.prototype = Object.create(SumsqN.prototype);
    sumsq.prototype.operationName = operationName;
}


function Sumsq2(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq2, "sumsq2");

function Sumsq3(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq3, "sumsq3");

function Sumsq4(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq4, "sumsq4");

function Sumsq5(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq5, "sumsq5");

function DistanceN(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    DistanceN,
    "distance",
    (...args) => Math.sqrt(args.reduce((ans, x) => ans += x * x, 0)),
    function(varName, ...args) {
        return new Divide(new SumsqN(...args).diff(varName), new Multiply(new Const(2), this));
    }
);

function NewDistanceN(distance, operationName) {
    distance.prototype = Object.create(DistanceN.prototype);
    distance.prototype.operationName = operationName;
}

function Distance2(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance2, "distance2");

function Distance3(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance3, "distance3");

function Distance4(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance4, "distance4");

function Distance5(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance5, "distance5");

const strToOperation = new Map([
    ["+", [Add, 2]],
    ["-", [Subtract, 2]],
    ["*", [Multiply, 2]],
    ["/", [Divide, 2]],
    ["negate", [Negate, 1]],
    ["sumsq2", [Sumsq2, 2]],
    ["sumsq3", [Sumsq3, 3]],
    ["sumsq4", [Sumsq4, 4]],
    ["sumsq5", [Sumsq5, 5]],
    ["distance2", [Distance2, 2]],
    ["distance3", [Distance3, 3]],
    ["distance4", [Distance4, 4]],
    ["distance5", [Distance5, 5]]
]);

const parseToken = (token, stack) => {
    if (strToOperation.has(token)) {
        return strToOperation.get(token);
    }
    stack.push(token);
    if (variableToIndex.has(token)) {
        return [Variable, 1];
    }
    return [Const, 1];
}

const parse = (expression) => {
    return expression.split(" ").filter(str => str !== "").reduce(
        (stack, str) => {
            const token = parseToken(str, stack);
            stack.push(new token[0](...stack.splice(-token[1])));
            return stack;
        }, []
    ).pop();
}

