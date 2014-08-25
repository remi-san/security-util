package net.remisan.security.permission;

public class PermissionUtil {

    public static final int READ = 1 << 0;
    public static final int WRITE = 1 << 1;
    public static final int CREATE = 1 << 2;
    public static final int DELETE = 1 << 3;
    public static final int ADMINISTRATION = 1 << 4;

    public static final int READ_WRITE = READ + WRITE;
    public static final int CREATE_DELETE = CREATE + DELETE;

    public static final int RWCD = READ_WRITE + CREATE_DELETE;
    public static final int COMPLETE_ADMIN = RWCD + ADMINISTRATION;

    protected PermissionUtil() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
}
