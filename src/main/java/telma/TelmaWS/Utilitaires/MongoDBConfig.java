package telma.TelmaWS.Utilitaires;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBConfig extends AbstractMongoClientConfiguration {

	@Override
	protected String getDatabaseName() {
		return "TelmaMongo";
	}
	
	public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://telma:1234@cluster0.1xcf6.mongodb.net/TelmaMongo?retryWrites=true&w=majority");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
	
}
