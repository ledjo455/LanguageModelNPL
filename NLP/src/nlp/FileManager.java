package nlp;


import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileManager {

    public static FileManager fileManager;
    private final File folder;
    private String language; // the language which is more similar to the mystery file

    private FileManager(File folder){
        this.folder = folder;
        language = "Not found";
    }

    public File getMysteryFile(){
        return new File(folder, "mystery.txt");
    }

    public static boolean createFileManager(String folderPath){
        File folder = new File(folderPath);
        if(validateFolder(folder)){
            fileManager = new FileManager(folder);
            return true;
        }
        return false;
    }

    public static FileManager getFileManager(){
        return fileManager;
    }

    private static boolean validateFolder(File folder){

        if(!folder.exists()){
            System.out.println("The given folder: " + folder.getAbsolutePath() + " does not exist");
            return false;
        }

        if(!folder.canRead()){
            System.out.println("Permission to read denied");
            return false;
        }

        if( !(new File(folder, "mystery.txt").exists()) ){
            System.out.println("The file mystery.txt does not exist");
            return false;
        }

        return true;
    }

    /**
     * calculates the model of each language
     * gives the closest model on the variable "language"
     *
     * */
    public void calculateModelLanguages(int nGram, ModelFile mysteryModel) {

        List<ModelLanguage> languages = Arrays.stream(
                Objects.requireNonNull(folder.list((folder, subItem) -> new File(folder, subItem).isDirectory())) // all sub-directories
        ).map(str -> new ModelLanguage(new File(folder, str), nGram, mysteryModel)) // create a VectorLanguage/thread for each directory
                .collect(Collectors.toList()); //toList

        if(languages.size() == 0){
            language = "None";
            return;
        }

        new ThreadPool().executeAndAwait(languages); // executes all folder threads and waits until they finish

        Optional<ModelLanguage> min = languages.stream().min((v1, v2) -> v1.compare(v1, v2));

        ModelLanguage rs = min.orElse(new ModelLanguage(new File("None"),-1, null));

        language = String.format("Nearest: %s angle: %f", rs.getNameFolder(), Math.toDegrees(rs.getVectorDistance()));

    }


    public String getResult() {
        return language;
    }
}
