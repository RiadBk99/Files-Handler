package View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.Control;


public class SerializationHelper {
	private static final String filePath = "MyBusiness.ser";

	// Save Hospital object to file
    public static void saveMyBusiness(Control MyBusiness) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(MyBusiness);
            System.out.println("MyBusiness object saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load MyBusiness object from file
    public static Control loadMyBusiness() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File " + filePath + " does not exist.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Control myBusiness = (Control) ois.readObject();
            System.out.println("MyBusiness object loaded successfully!");
            return myBusiness;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}