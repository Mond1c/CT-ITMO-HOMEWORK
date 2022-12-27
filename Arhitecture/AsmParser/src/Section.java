public class Section {
    public final int name;
    public final int type;
    public final int flags;
    public final int addr;
    public final int offset;
    public final int size;
    public final int link;
    public final int info;
    public final int addrAlign;
    public final int entSize;

    public Section(final int name, final int type, final int flags, final int addr, final int offset,
                   final int size, final int link, final int info, final int addrAlign, final int entSize) {
        this.name = name;
        this.type = type;
        this.flags = flags;
        this.addr = addr;
        this.offset = offset;
        this.size = size;
        this.link = link;
        this.info = info;
        this.addrAlign = addrAlign;
        this.entSize = entSize;
    }
}
