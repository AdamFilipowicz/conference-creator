package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Config;
import com.afi.repository.ConfigRepository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ConfigService {
	
	private static final String PATH = "C:\\kreator_konferencji\\nowa";
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private ConfigRepository configRepository;

	@Autowired
    public ConfigService(EntityManager entityManager, ConfigRepository configRepository) {
		this.entityManager = entityManager;
		this.configRepository = configRepository;
	}
	
	public List<String> findConfig(Conference conference){
		List<String> list = new ArrayList<String>();
		list.add(findValueByKeyAndConference("image_path", conference));
		return list;
	}
	
	public void saveConfig(List<Object> config, Conference conference) {
		updateValueByKeyAndConference("image_path", config.get(0).toString(), conference);
	}
    
    public Config updateConfig(Config config, Conference conference) {
    	config.setConference(conference);
    	Config saved = configRepository.save(config);
        return saved;
    }
    
    public String findValueByKeyAndConference(String key, Conference conference) {
    	Config conf = configRepository.findByKeyAndConference(key, conference);
    	if(conf == null) {
    		conf = new Config(key, getDefault(key));
    		conf.setConference(conference);
    		configRepository.save(conf);
    	}
    	return conf.getValue();
    }
    
    public void updateValueByKeyAndConference(String key, String value, Conference conference) {
    	Config conf = configRepository.findByKeyAndConference(key, conference);
    	if(conf == null) {
    		conf = new Config(key, value);
    		conf.setConference(conference);
    		configRepository.save(conf);
    	}
    	else {
    		conf.setValue(value);
    	}
    	configRepository.save(conf);
    }
    
    public void deleteConfigById(long id) {
        configRepository.delete(configRepository.findById(id).orElse(null));
    }
    
    private String getDefault(String key) {
    	if(key == "image_path") {
    		return PATH;
    	}
    	return "";
    }
}
