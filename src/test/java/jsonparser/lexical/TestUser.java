package jsonparser.lexical;

public final class TestUser {

    private String name;

    private int id;

    public TestUser(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public TestUser() {

    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof TestUser)) {
            return false;
        }

        TestUser other = (TestUser) object;
        return name.equals(other.name) && id == other.id;
    }

    @Override
    public String toString() {
        return name + ": " + id;
    }

}