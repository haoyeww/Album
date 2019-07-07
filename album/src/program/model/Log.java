package program.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Log implements Serializable{
    // the ArrayList of All the logs
    private ArrayList<ArrayList<String>> allLogs = new ArrayList<>();

    // the ArrayList of all the tags
    private ArrayList<String> allTags = new ArrayList<>();

    // the HashMap used to store information for userName and password
    private HashMap<String, String> userInfo = new HashMap<>();
    // the singleton filed so only one Log is created
    private static Log firstInstance = null;

    /**
     * the getInstance method here is used to check if the Log class is created
     * if it's created once, it will just use the created one.
     * @return return the firstInstance that is a Log object created at first
     */
    public static Log getInstance() {
        if (firstInstance == null) {
            firstInstance = new Log();
        }
        return firstInstance;
    }

  /**
   * This Log constructor simply read the Save,bin file that contains all the logs for all the
   * images
   */
  private Log() {
        readFile("Save.bin",allLogs);
        readFile("Tag.bin", allTags);
        readFile("Password.bin", userInfo);
    }


    /**
     * the addLog method take 3 input and record them into the log file
     * the 3 input are imageName before change and imageName after change
     * the time is also recorded for reverting operation
     * @param beforeChange imageName before change
     * @param afterChange imageName after change
     * @param time the time of this change
     */
    void addLog(String beforeChange, String afterChange, String time) {
        ArrayList<String> logItem = new ArrayList<>();
        logItem.add(beforeChange);
        logItem.add(afterChange);
        logItem.add(time);
        allLogs.add(logItem);
        writeToFile("Save.bin",allLogs);
    }

    /**
     *the writeToFile method is a help method to put the logs(ArrayList)
     * into the configuration file(save.bin)
     * @param logs the log we want to write into the configuration file
     */
     private void writeToFile(String pathName,Object logs) {
        try {
            File save = new File(pathName);
            FileOutputStream fileOutputStream = new FileOutputStream(save);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(logs);
            // System.out.println("write successful for"+pathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * the readFile method is most important method in Log and it is used in constructor
     * this method will create the configuration file Save.bin if there is no such file
     * and write allLogs into it. If there is a configuration file, then the logs inside file
     * is read and output to allLogs
     */
    private void readFile(String pathName, Object logs) {
        try {
            File f = new File(pathName);
            if (!f.exists() || f.isDirectory()) {
                // the result of createNewFile is not necessary need
                if(f.createNewFile()) {
                    writeToFile(pathName, logs);
                }
            } else {
                FileInputStream fileInputStream = new FileInputStream(pathName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // the type is checked using logs.equals since instanceof can not check generic type
                if (logs.equals(allLogs)) {
                    allLogs = (ArrayList<ArrayList<String>>) objectInputStream.readObject();
                } else if (logs.equals(getAllTags())){
                    setAllTags((ArrayList<String>) objectInputStream.readObject());
                } else if (logs.equals(userInfo)) {
                    userInfo = ((HashMap<String, String>) objectInputStream.readObject());
                }
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    /**
     * the get method for allLogs
     * @return return allLogs
     */
    ArrayList<ArrayList<String>> getAllLogs() {
        return allLogs;
    }

    /**
     * the method getLogs returns a specific sets of logs only for a given ImageFile
     * @param image an ImageFile; we want to find a logs only of this ImageFile
     * @return ArrayList of ArrayList of String  the Array of log that records every name change for this ImageFile
     */
    public ArrayList<ArrayList<String>> getLogs(ImageFile image) {
        ArrayList<ArrayList<String>> log = new ArrayList<>();
        String[] nameParts = image.getOriginalName().split("\\.");
        for (ArrayList<String> logs : allLogs){
            if (logs.get(0).contains(nameParts[0]) && logs.get(0).contains(nameParts[1])){
                log.add(logs);
            }
        }
        return log;
    }

    /**
     * he scanTags method split image's name into parts and check if each part is a tag contains @
     * then if a tag is found , it will be added into the ArrayList tags.
     * @param image the target ImageFile one wants to scan and update
     */
    void scanTags(ImageFile image) {
        image.setTags(new ArrayList<>());
        String pureName = image.getImageName().split("\\.")[0];
        String[] pureParts = pureName.split(" ");
        for (String parts : pureParts) {
            if (parts.contains("@")) {
                String tag = parts.replace("@", "");
                Tag T = TagManager.checkExists(tag);
                image.addToTags(T);
                new ImageEditor(image).addNewTag(T);
                if (!getAllTags().contains(tag)) {
                    getAllTags().add(tag);
                }
            }
        }
        writeToFile("Tag.bin", getAllTags());
    }

    /**
     * the get method for allLogs
     * @return return allLogs
     */
    ArrayList<String> getAllTags() {
        return allTags;
    }

    /**
     * the set method for allLogs
     */
    private void setAllTags(ArrayList<String> allTags) {
        this.allTags = allTags;
    }


    /**
     * the method to add a Tag's name into allTags List
     * @param T the Tag object one wants to add
     */
    void addToAllTags(Tag T){
        this.allTags.add(T.getTagName());
    }

    /**
     * the method to add a username and a password into the userInfo hashMap
     * @param user the user name in String format
     * @param password the user password in String format
     */
    void addUserInfo(String user, String password) {
        if (!userInfo.containsKey(user)) {
            userInfo.put(user, password);
        } else {
            System.out.println("User already exist!");
        }
        writeToFile("Password.bin", userInfo);
    }

    /**
     * this method is to check if target
     * @param  user is the username one wants to check
     * @return return true if it contains target username, fail otherwise
     */
    public boolean userExists(String user) {
        return userInfo.containsKey(user);
    }

    /**
     *  this method is used to find the user's password
     * @param user the username one wants to find it's password
     * @return return the password matches the user
     */
    String getUserPassword(String user) {
        return userInfo.get(user);
    }
}
