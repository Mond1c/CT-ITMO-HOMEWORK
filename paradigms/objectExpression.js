"use strict"

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

class PartOfExpression {
    evaluate(...args) {

    }

    diff(varName) {

    }

    toString() {

    }
}

class Const extends PartOfExpression {
    constructor(x) {
        super();
        this.x = x;
    }

    evaluate(...args) {
        return Number(this.x);
    }

    diff(varName) {
        return new Const(0);
    }

    toString() {
        return this.x.toString();
    }
}

class Variable extends PartOfExpression {
    constructor(varName) {
        super();
        this.varName = varName;
    }

    evaluate(...args) {
        return args[variableToIndex.get(this.varName)];
    }

    diff(varName) {
        return this.varName === varName ? new Const(1) : new Const(0);
    }

    toString() {
        return this.varName;
    }
}

class Operation extends PartOfExpression {
    constructor(operationName, ...parts) {
        super();
        this.operationName = operationName;
        this.parts = parts;
    }

    calculate(...args) {

    }

    diffOperation(varName, ...args) {

    }

    evaluate(...args) {
        return this.calculate(...this.parts.map(part => part.evaluate(...args)));
    }

    diff(varName) {
        return this.diffOperation(varName, ...this.parts);
    }

    toString() {
        return this.parts.map(part => part.toString()).join(' ') + " " + this.operationName;
    }
}

class Add extends Operation {
    constructor(...parts) {
        super("+", ...parts);
    }

    calculate(...args) {
        return args.reduce((ans, x) => ans += x, 0);
    }

    diffOperation(varName, ...args) {
        return new Add(...args.map(part => part.diff(varName)));
    }
}

class Subtract extends Operation {
    constructor(...parts) {
        super("-", ...parts);
    }

    calculate(x, y) {
        return x - y;
    }

    diffOperation(varName, left, right) {
        return new Subtract(left.diff(varName), right.diff(varName));
    }
}

class Multiply extends Operation {
    constructor(...parts) {
        super("*", ...parts);
    }

    calculate(x, y) {
        return x * y;
    }

    diffOperation(varName, left, right) {
        return new Add(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        );
    }
}


class Divide extends Operation {
    constructor(...parts) {
        super("/", ...parts);
    }

    calculate(x, y) {
        return x / y;
    }

    diffOperation(varName, left, right) {
        return new Divide(
            new Subtract(
                new Multiply(left.diff(varName), right),
                new Multiply(left, right.diff(varName))
            ),
            new Multiply(right, right)
        );
    }
}

class Negate extends Operation {
    constructor(...parts) {
        super("negate", ...parts);
    }

    calculate(x) {
        return -x;
    }

    diffOperation(varName, part) {
        return new Negate(part.diff(varName));
    }
}

class SumsqN extends Operation {
    constructor(n, ...parts) {
        super("sumsq" + n, ...parts);
    }

    calculate(...args) {
        return args.reduce((ans, x) => ans += x * x, 0);
    }

    diffOperation(varName, ...args) {
        return new Add(...args.map(part => new Multiply(part, part).diff(varName)));
    }
}

class Sumsq2 extends SumsqN {
    constructor(...parts) {
        super(2, ...parts);
    }
}

class Sumsq3 extends SumsqN {
    constructor(...parts) {
        super(3, ...parts);
    }
}

class Sumsq4 extends SumsqN {
    constructor(...parts) {
        super(4, ...parts);
    }
}

class Sumsq5 extends SumsqN {
    constructor(...parts){
        super(5, ...parts);
    }
}

class DistanceN extends Operation {
    constructor(n, ...parts) {
        super("distance" + n, ...parts);
        this.n = n;
    }

    calculate(...args) {
        return Math.sqrt(args.reduce((ans, x) => ans += x * x, 0));
    }

    diffOperation(varName, ...args) {
        return new Divide(new SumsqN(this.n, ...args).diff(varName), new Multiply(new Const(2), this));
    }
}

class Distance2 extends DistanceN {
    constructor(...parts) {
        super(2, ...parts);
    }
}

class Distance3 extends DistanceN {
    constructor(...parts) {
        super(3, ...parts);
    }
}

class Distance4 extends DistanceN {
    constructor(...parts)  {
        super(4, ...parts);
    }
}

class Distance5 extends DistanceN {
    constructor(...parts) {
        super(5, ...parts);
    }
}

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
