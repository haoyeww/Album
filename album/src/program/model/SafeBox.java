package program.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SafeBox {

  // the singleton field
  private static SafeBox firstInstance = null;
  // HashMap used to store username and it's images
  private HashMap<String, ArrayList<ImageFile>> safe = new HashMap<>();
  // this user's directory
  private Directory dir;

  /**
   * the constructor creates a file for secured images
   */
  private SafeBox() {
    File folder = new File("safebox");

    if (!folder.exists()) {
      folder.mkdir();
    }
  }

  /**
   * this method check if the SafeBox object is already created
   * if not it will create a new one
   */
  public static SafeBox getInstance() {
    if (firstInstance == null) {
      firstInstance = new SafeBox();
    }
    return firstInstance;
  }

  /**
   * this method update the information of user's secured file
   * @param name the username one wants to refresh
   */
  private void refreshBox(String name) {
    dir = new Directory("safebox/" + name);
    safe.put(name, dir.getImageFile());
  }


  /**
   * the method used to add a new user with username and password
   * if the username is already exist, it will print a message to user
   * @param name the username one wants to create
   * @param password the password one wants to create
   */
  public void addUser(String name, String password) {
    if (!safe.containsKey(name)) {
      safe.put(name, new ArrayList<>());
      File folder = new File("safebox/" + name);
      if (!folder.exists()) {
        folder.mkdir();
      }
      Log.getInstance().addUserInfo(name, password);
    } else {
      System.out.println("User already exists!");
    }
  }

  /**
   * this method is used to add target ImageFile into user's secured directory
   * @param image the ImageFile one wants to move
   * @param user the file will be moved to this user's secured directory
   */
  public void addSafeImage(ImageFile image, String user) {
    refreshBox(user);
    dir.moveFile(image, "safebox/" + user);
  }

  /**
   * this method is used to check if the password is wright for this user
   * @param user the username one wants to check
   * @param password the password one wants to check
   * @return return true if the password is true and false otherwise
   */
  public boolean passwordCheck(String user, String password) {
    return Log.getInstance().getUserPassword(user).equals(password);
  }

  /**
   * this method is used to check if the password matched our pattern
   * @param password the password one wants to chekc
   * @return return true if the password has the pattern one wants otherwise return false
   */
  public boolean passwordFormat(String password) {
    String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(password);
    return m.find();
  }
}
