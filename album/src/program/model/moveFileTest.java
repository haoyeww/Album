package program.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class moveFileTest {
    private static Directory D;
    private static ImageFile I;
    private File F;

    @BeforeAll
    static void beforeAll(){
        D = new Directory("phase2/unitTestImage");
        I = D.getImageFile().get(3);
    }

    @BeforeEach
    void beforeEach(){
        D.moveFile(I, "phase2/unitTestImage/image1");
        D.initiateImage();
        F = new File("phase2/unitTestImage/image1/aaa @cat.jpg");

    }

    @AfterEach
    void afterEach(){
        D.moveFile(I, "phase2/unitTestImage");
    }

    @org.junit.jupiter.api.Test
    void testMoveFile() { assertEquals(F.getName(), I.getImageName()); }

    @org.junit.jupiter.api.Test
    void testMoveFile2() {
        assertEquals(F.toString(), I.getLocation());
    }

    @org.junit.jupiter.api.Test
    void testMoveFile3() {
        assertEquals(F.toString(), I.getImage().toString());
    }

    @org.junit.jupiter.api.Test
    void testMoveFile4(){ assertEquals(F,I.getImage()); }

}
