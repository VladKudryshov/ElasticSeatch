
public class Parser {

    public Parser() {
    }

    public String[] parse(String str){
        String words[] = str.split("[- ,;:.!?\n\"\\\\()]+");
        return words;
    }

}
