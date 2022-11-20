public enum Cell {
    E {
        @Override
        public String toString() {
            return ".";
        }
    },
    X {
        @Override
        public String toString() {
            return "X";
        }
    },
    O {
        @Override
        public String toString() {
            return "0";
        }
    },
}
