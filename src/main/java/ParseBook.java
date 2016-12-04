import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.util.ArrayList;


public class ParseBook {
    public ParseBook() {
    }

    public ArrayList<String[]> GetWordsPdf(String pathBook){
        PdfReader reader = null;
        Parser pr = new Parser();
        ArrayList<String[]> words = new ArrayList<String[]>();
        try {
            reader = new PdfReader(pathBook);
            int countPages = reader.getNumberOfPages();
            for (int i = 2; i <= countPages; i++) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                String[] temp = pr.parse(PdfTextExtractor.getTextFromPage(reader, i, strategy));
                words.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return words;
    }

}
