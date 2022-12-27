public class SymTableEntity {
    public final String name;
    public final String type;
    public final String visibility;
    public final String id;
    public final String bind;

    public int value;
    public int size;
    public int symbol = 0;

    public SymTableEntity(final String name, final int value, final int size, final int symbol,
                          final int info, final int other) {
        this.name = name;
        this.value = value;
        this.size = size;
        this.symbol = symbol;
        this.type = SymTableParser.getType(info & 0xf);
        this.visibility = SymTableParser.getVisibility(other & 0x3);
        this.id = SymTableParser.getId(other >> 8);
        this.bind = SymTableParser.getBind(info >> 4);
    }

    public SymTableEntity(final String name) {
        this.name = name;
        this.type = "FUNC";
        this.visibility = "";
        this.id = "";
        this.bind = "";
    }

    @Override
    public String toString() {
        if (name.startsWith("LOC")) {
            return "";
        }
        return String.format("[%4i] 0x%-15X %5i %-8s %-8s %-8s %6s %s\n",
                symbol, value, size, type, bind, visibility, id, name);
    }
}
