package program.model;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Directory {
    //  file of target location
    private File file;
    // the ArrayList of all the ImageFile inside directory.
    private ArrayList<ImageFile> imageFile = new ArrayList<>();
    // the ArrayList of all the images' names.
    private ArrayList<String> imageName = new ArrayList<>();
    // the ArrayList of all the format of image we want
    private ArrayList<String> picFormat = new ArrayList<>(Arrays.asList("jpg", "jfif", "gif", "png", "tiff", "jpeg", "tif"));

    /**
     * construct a new directory object by inputting an existing directory location
     * location is the real location of the file(eg:"C:\Users\Administrator\Desktop\group_0480\phase1\src\image\image2")
     * the imageName ArrayList and imageFile ArrayList are initiate in this constructor
     * @param path path is the location of the chosen directory
     */
    public Directory(String path) {
        setFile(new File(path));
        initiateImage();
        initiateName();
    }

    /**
     *initiateImage method is a recursion method that searches all the image files anywhere under the directory
     * once target file is found , it is then cast to an ImageFile class for future use
     * @return the method returns an ArrayList of all the ImageFile anywhere under the directory
     */

    ArrayList<ImageFile> initiateImage() {
        ArrayList<ImageFile> A = new ArrayList<>();
        ArrayList<ImageFile> B = new ArrayList<>();
        // the file is assumed to be a directory contains files so the path should not be null  
        for (final File f : getFile().listFiles()){
            if (!f.isDirectory()){
                for (String formats : picFormat){
                    if (formats.equals(findFormat(f))){
                        ImageFile I = new ImageFile(f);
                        A.add(I);
                    }
                }
            }
            else if (f.isDirectory()  && !f.getName().equals("safebox")){
                Directory F = new Directory(f.toString());
                B.addAll(F.initiateImage());

            }
        }
        ArrayList<ImageFile> allFiles = new ArrayList<>();
        allFiles.addAll(B);
        allFiles.addAll(A);
        getImageFile().addAll(allFiles);
        return allFiles;
    }

    /**
     * This method update the imageName ArrayList by adding all the name from ImageFile in imageFile ArrayList.A
     */

    void initiateName() {
        for (ImageFile I : getImageFile()){
            getImageName().add(I.getImageName());
        }
    }

    /**
     *moveFile method moves an input file inside the imageFile list to a given path
     * @param F ImageFile that we want to move
     * @param moveTo the destination location (eg: "/home/haoye/IdeaProjects/group_0480/phase1/src/image")
     */
    public void moveFile(ImageFile F, String moveTo){
        String newPath;
        if (F.getImage().toString().contains("/")) {
            newPath = moveTo + "/" + F.getImageName();
        } else {
            newPath = moveTo + "\\" + F.getImageName();
        }
        File dest = new File(newPath);
        // the result of renameTo can be ignored since the result is just a boolean and it's not important.
        if (F.getImage().renameTo(dest)){
            F.setImage(new File(newPath));
            F.setLocation(newPath);

        }

    }

    /**
     * searchImage method searches through the imageFile list and check if there is a imageFiles has the same name as input.
     * @param name String name of the image we wants to find
     * @return return the correct ImageFile or null if there is no such ImageFile.
     */
    ImageFile searchImage(String name){
        for (ImageFile I : this.getImageFile()){
            if ( I.getImageName().equals(name)){
                return (I);
            }
        }
        return null;
    }

    /**
     * The method findFormat takes a file as input and will look for file's postfix
     * which i called it's format(eg: like jpg,jfif,jpeg etc)
     * @param F f is the file that we want to find its format
     * @return return the string representation of file's format
     */
    String findFormat(File F) {
        String[] parts = F.getName().split("\\.");
        return parts[parts.length-1];
    }


    /**
     * the get method for filed File
     * @return return this Directory's actual File
     */
    public File getFile() {
        return file;
    }

    /**
     * the set method for filed File
     * @param file the file one wants to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * the get method for ArrayList imageFile
     * @return return this Directory's imageFile
     */
    public ArrayList<ImageFile> getImageFile() {
        return imageFile;
    }

    /**
     * the set method for imageFile List
     * @param imageFile the List one wants to set
     */
    public void setImageFile(ArrayList<ImageFile> imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * get method for ImageName List
     * @return return this Directory's imageName List
     */
    public ArrayList<String> getImageName() {
        return imageName;
    }

    /**
     * the set method for imageName List
     * @param imageName the imageName List one wants to set
     */
    void setImageName(ArrayList<String> imageName) {
        this.imageName = imageName;
    }

}
