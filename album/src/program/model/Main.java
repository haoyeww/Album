package program.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" Enter the directory you want to goto. ");
        String input = keyboard.readLine();
        Directory directory = new Directory(input);
        System.out.println("Here is a list of image name in target directory:");
        System.out.println(directory.getImageName());
        System.out.println("Please enter the name of the image you want to edit:");
        String imageName = keyboard.readLine();
        ImageFile image = directory.searchImage(imageName);
        System.out.println("Please enter action: ");
        String action = keyboard.readLine();
        while (!action.equals("exit")) {
            switch (action) {
                case "addTag":
                    System.out.println("Please enter the tag you want to add: ");
                    String tagAdd = keyboard.readLine();
                    Tag T = TagManager.checkExists(tagAdd);
                    new ImageEditor(image).addNewTag(T);
                    directory.setImageName(new ArrayList<>());
                    directory.initiateName();
                    break;
                case "deleteTag":
                    System.out.println(image.getTagNames());
                    System.out.println("Please enter the tag you want to delete: ");
                    String tagDel = keyboard.readLine();
                    Tag T2 = TagManager.checkExists(tagDel);
                    new ImageEditor(image).deleteTag(T2);
                    directory.setImageName(new ArrayList<>());
                    directory.initiateName();
                    break;
                case "location":
                    System.out.println(image.getLocation());
                    break;
                case "originalName":
                    System.out.println(image.getOriginalName());
                    break;
                case "getLogs":
                    System.out.println(Log.getInstance().getLogs(image));
                    break;
                case "viewimages":
                    System.out.println(directory.getImageFile());
                    break;
                case "goBack":
                    System.out.println(Log.getInstance().getAllLogs());
                    System.out.println(" Enter the timestamp you want to revert back to. ");
                    String time = keyboard.readLine();
                    new ImageEditor(image).goBack(time);
                    break;
                case "moveImage":
                    if (image == null){
                        System.out.println("Target image does not exist please check location and spelling" +
                                "(correct example: @cat p1.jpg)");
                        break;
                    }
                    System.out.println("Please enter the destination you want to move:");
                    String dest = keyboard.readLine();
                    directory.moveFile(image, dest);
                    break;
                case "showByTag":
                    System.out.println("Enter the tag you want to search: ");
                    String tag = keyboard.readLine();
                    Tag T3 = TagManager.checkExists(tag);
                    System.out.println(T3.getImages());
                    break;
                case "allTags":
                    System.out.println(Log.getInstance().getAllTags());
                    break;
                case "getTags":
                    System.out.println(image.getTagNames());
                    break;
                case "test":
                    for (ImageFile I2:image.getTags().get(0).getImages()){
                        System.out.println(I2.getImageName());
                    }
                case "tag test":
                    System.out.println(image.getTags().get(0).getImages());
                    break;
                case "addUser":
                    System.out.println("Enter username:");
                    String username = keyboard.readLine();
                    System.out.println("Enter username:");
                    String pwd = keyboard.readLine();
                    while (!SafeBox.getInstance().passwordFormat(pwd)) {
                        System.out.println("Password is not valid!");
                        pwd = keyboard.readLine();
                    }
                    SafeBox.getInstance().addUser(username,pwd);
                    System.out.println("User added!");
                    break;
                case "addToSafe":
                    System.out.println("Enter username:");
                    String name = keyboard.readLine();
                    SafeBox.getInstance().addSafeImage(image,name);
                    break;
                case "check Tag 1":
                    T = TagManager.checkExists("cat");
                    System.out.println(T.getImages());
                    for (ImageFile I : T.getImages()) {
                        System.out.println(I.getImageName());
                    }
                case "imageName":
                    System.out.println(image.getImageName());
                default:
                    System.out.println("Please enter the correct command.");
                    break;

            }
            System.out.println("Here is a list of image name in target directory:");
            directory = new Directory(input);
            System.out.println(directory.getImageName());
            System.out.println("Please enter the name of the image you want to edit:");
            imageName = keyboard.readLine();
            image = directory.searchImage(imageName);
            System.out.println("Please enter action: ");
            action = keyboard.readLine();
        }
    }
}