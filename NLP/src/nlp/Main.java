package nlp;


public class Main extends Thread{

    private final String folderPath;
    private final int nGram;

    public Main(String folderPath, int nGram) {
        this.folderPath = folderPath;
        this.nGram = nGram;
    }

    @Override
    public void run() {

        if(!FileManager.createFileManager(folderPath))
            System.exit(1);

        ModelFile mysteryVector = new ModelFile(FileManager.getFileManager().getMysteryFile(), nGram);
        mysteryVector.start();

        FileManager.getFileManager().calculateModelLanguages(nGram, mysteryVector);

        System.out.println(FileManager.getFileManager().getResult());

    }

    public static void main(String[] args) {

    	//----------------- Purpose of testing -----------------------
     //   args = new String[]{"C:\\Users\\ledjo\\Documents\\NetBeansProjects\\AdvancedJavaAss1\\LocalFolder", "2"}; // path, n-gram
        //----------------- end Purpose of testing ----------------------

        
        if(args.length == 0){
            System.err.println("There is no folder path");
            System.exit(1);
        }

        int nGram = 2;

        if(args.length == 2){
            try {
                nGram = Integer.parseInt(args[1]);
                if (nGram < 1) throw new NumberFormatException();

            }catch (NumberFormatException e){
                System.err.println("Invalid number");
                System.exit(1);
            }
        }

       new Main(args[0], nGram).start();

    }

}
