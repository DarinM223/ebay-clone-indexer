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
 
    public void rebuildIndexes() {
        Connection conn = null;
        IndexWriter iw = null;

        // create a connection to the database to retrieve Items from MySQL
	    try {
	        conn = DbManager.getConnection(true);
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }

        //create lucene indexwriter
        try {
            iw = new IndexWriter(System.getenv("LUCENE_INDEX"),
                new StandardAnalyzer(), true);
        }
        catch (IOException e) {
            System.out.println(ex);
        }

        

        // close the database connection
	    try {
	        conn.close();
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
