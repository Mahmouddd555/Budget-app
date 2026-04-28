package database;

public class Database {
    private static Database instance;

    private Database() {}

    public static Database getInstance() {
        return instance;
    }

    public void connect() {}

    public void disconnect() {}
}