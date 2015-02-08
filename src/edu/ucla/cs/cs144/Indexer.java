package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(indexDir, config);
        }

        return indexWriter;
   }

    public void indexItem (int id, String name, String description, String categories) throws IOException {
        IndexWriter iw = getIndexWriter();
        Document doc = new Document();

        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new StringField("name", name, Field.Store.YES));
        doc.add(new StringField("description", description, Field.Store.YES));
        doc.add(new TextField("categories", categories, Field.Store.NO));
        
        iw.addDocument(doc);
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }   
 
    public void rebuildIndexes() throws SQLException, IOException {
        Connection conn = null;        

        // create a connection to the database to retrieve Items from MySQL
	    conn = DbManager.getConnection(true);

        //erase existing index
        getIndexWriter();

        //query the database
        Statement statement = conn.createStatement();

        String query = "SELECT Item.ItemID, Item.Name, Item.Description, Categories.Categories "
                        + "FROM ( "
                            + "SELECT ItemID "
                            + "FROM ItemCategory "
                            + "GROUP BY ItemID) AS Categories "
                        + "INNER JOIN Item "
                        + "ON Item.ItemID = Categories.ItemID";

        //get the goods
        ResultSet items = statement.executeQuery(query);

        //index each item
        while (items.next()) {
            indexItem(items.getInt("ItemID"), items.getString("Name")
                        items.getString("Description"), items.getString("Categories"));
        }

        //close index writer
        closeIndexWriter();

        // close the database connection
	    conn.close();

    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        try {
            idx.rebuildIndexes();
        }
        catch (Exception e) {
            System.out.prinln(e);
        }
    }   
}
