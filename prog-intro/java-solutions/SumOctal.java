public class SumOctal {
    public static void main(String[] args) {
        int sum = 0;
        for (String arg : args) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean isOctal = false;
            for (int i = 0; i < arg.length(); i++) {
                boolean characterIsWhitespace = Character.isWhitespace(arg.charAt(i));
                if (!characterIsWhitespace) {
                    if (Character.toLowerCase(arg.charAt(i)) == 'o') {
                        isOctal = true;
                    } else {
                        stringBuilder.append(arg.charAt(i));
                    }
                }
                if (characterIsWhitespace || i == arg.length() - 1) {
                    if (!stringBuilder.isEmpty()) {
                        if (isOctal) {
                            sum += Integer.parseUnsignedInt(stringBuilder.toString(), 8);
                        } else {
                            sum += Integer.parseInt(stringBuilder.toString());
                        }
                        stringBuilder = new StringBuilder();
                        isOctal = false;
                    }
                }
            }
        }
        System.out.println(sum);
    }
}
