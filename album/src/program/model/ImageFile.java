package program.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ImageFile implements Serializable{
    // this ImageFile's  name
    private String imageName;
    // this ImageFile's original Name (the name without tags)
    private String originalName;
    // this ImageFile's current location
    private String location;
    // the ArrayList of all the tags in this ImageFile
    private ArrayList<Tag> tags = new ArrayList<>();
    // a File connect to the real file to get information
    private File image;

    /**
     * the constructor of the ImageFile class,imageName,originalName,location and image are updated from the input file
     * and tags of target file(if exist) are scanned and putted into the tags ArrayList
     * @param file the input is a file object represent the image file
     */
    ImageFile(File file){
        setImageName(file.getName());
        setLocation(file.toString());
        setImage(file);
        String pureName = this.getImageName().split("\\.")[0];
        String[] pureParts = pureName.split(" ");
        setOriginalName(pureParts[0] + "." + this.getImageName().split("\\.")[1]);
        // System.out.println("scan for" + this.getImageName());
        Log.getInstance().scanTags(this);
        // System.out.println("scan finished");
    }

    ArrayList<String> getTagNames(){
        ArrayList<String> A = new ArrayList<>();
        for (Tag T: tags){
            A.add(T.getTagName());
        }
        return A;
    }

    /**
     * the get method for OriginalName
     * @return return the string of originalName
     */
    public String getOriginalName() {
        return originalName;
    }


    /**
     * the get method for imageName
     * @return return the string of imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * the get method for location
     * @return return the string location
     */
    public String getLocation() {
        return location;
    }

    /**
     * equal method to check if ImageFile i is the same as this ImageFile
     * @param I the ImageFile we want to compare with
     * @return boolean true if they are the same and false if they are not.
     */
    boolean equals(ImageFile I){
        return this.getImageName().equals(I.getImageName());
    }

    /**
     * the set method for location
     * it sets path to the new location
     */
    void setLocation(String path) {this.location = path;}


    /**
     * the get method for tags
     * @return return tags ArrayList
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * the set method for imageName
     */
    void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * the set method for originalName
     */
    private void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * the set method for tags
     */
    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    /**
     * the add method for tags
     * this method add tag t to tags ArrayList
     * @param T the tag we want to add to tags
     */
    void addToTags(Tag T){
        this.tags.add(T);
    }

    /**
     * the get method for image which is the actual image file
     * @return return the image file
     */
    public File getImage() {
        return image;
    }

    /**
     * the set method for image that is the actual file
     * @param image the file one wants to set
     */
    public void setImage(File image) {
        this.image = image;
    }


}
