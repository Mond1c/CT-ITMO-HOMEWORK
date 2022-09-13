public class Sum {
    public static void main(String[] args) {
        int sum = 0;
        for (String arg : args) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arg.length(); i++) {
                if (Character.isWhitespace(arg.charAt(i))) {
                    if (!stringBuilder.isEmpty()) {
                        sum += Integer.parseInt(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                    }
                } else {
                    stringBuilder.append(arg.charAt(i));
                }
            }
            if (!stringBuilder.isEmpty()) {
                sum += Integer.parseInt(stringBuilder.toString());
            }
        }
        System.out.println(sum);
    }
}