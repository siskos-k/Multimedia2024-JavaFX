public interface LibraryUser {
    boolean login(String username, String password);
    boolean hasViewingPrivileges();

   
    // void logout();
}
