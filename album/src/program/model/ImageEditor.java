package program.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ImageEditor {

    // the ImageFile this Editor is editing
    private ImageFile image;

    /**
     * the constructor of ImageEditor is simple, it just take the input ImageFile as it's field
     * then it can editing the given ImageFile
     * @param image the ImageFile one wants to edit
     */
    public ImageEditor(ImageFile image) {
        this.image = image;
    }
    /**
     * this method add a given tag onto the ImageFile
     * and the tag is also added to the tag list so userTxt can view all the tags in this ImageFile
     * @param tag a string like tag we want to add
     */
    public void addNewTag(Tag tag) {
        if (!image.getTags().contains(tag)) {
            image.getTags().add(tag);
            rename(tag, "add");
        }

        if(!tag.checkContain(image)){
            tag.getImages().add(image);
        }

        if(!Log.getInstance().getAllTags().contains(tag.getTagName())){
            Log.getInstance().addToAllTags(tag);
        }
    }

    /**
     * this method delete a given tag from this ImageFile
     * @param tag a tag string that we want to delete
     */
    public void deleteTag(Tag tag) {
        ImageFile toMove = null;
        for (ImageFile I : tag.getImages()){
            if (I.equals(image)){
                toMove = I;
            }
        }
        tag.getImages().remove(toMove);

        if (image.getTags().contains(tag)) {
            image.getTags().remove(tag);
            rename(tag, "remove");
        }
    }

    /**
     * goBack method takes a parameter time and revert the
     * ImageFile's name to the name at that time
     * @param time the string representation of the time
     */
    public void goBack(String time) {
        for (ArrayList<String> changes:Log.getInstance().getAllLogs()) {
            if (changes.contains(time)) {
                this.rename(changes.get(0), "revert");
                Log.getInstance().scanTags(image);
                break;
            }
        }
    }

    /**
     * the rename method is a help function used in deletingTag, addingTag and goBack methods
     * it has 2 String input of tag and function and it will either remove or add tag to the ImageFile
     * or it can revert the ImageFile to a older version
     * @param input the tag we want to add or delete on the ImageFile
     * @param function the operation we want to perform
     */
    private void rename(Object input, String function) {
        String beforeChange = image.getImageName();
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        String time  =  timeStamp.toString();
        switch (function) {
            case "remove":
                image.setImageName(image.getImageName().replace(" @" + ((Tag)input).getTagName(), ""));
                break;
            case "add":
                String[] parts = image.getImageName().split("\\.");
                image.setImageName(parts[0] + " @" + ((Tag)input).getTagName() + "." + parts[1]);
                break;
            case "revert":
                System.out.println((String)input);
                image.setImageName((String)input);
                break;
            default:
                break;
        }
        Log.getInstance().addLog(beforeChange,image.getImageName(),time);
        image.setLocation(image.getLocation().replace(beforeChange, image.getImageName()));
        File changeTo = new File(image.getLocation());
        if (image.getImage().renameTo(changeTo)) {
            System.out.println("Rename Successful!");
        } else {
            System.out.println("Rename Failed!");
        }
        image.setImage(changeTo);
    }
}
