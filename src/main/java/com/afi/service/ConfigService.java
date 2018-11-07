package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afi.data.TableData;
import com.afi.model.Conference;
import com.afi.model.Config;
import com.afi.repository.ConfigRepository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ConfigService {
	
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
		list.add(findValueByKeyAndConference("logo", conference));
		list.add(findValueByKeyAndConference("main_photo", conference));
		list.add(findValueByKeyAndConference("email", conference));
		list.add(findValueByKeyAndConference("phone", conference));
		list.add(findValueByKeyAndConference("address1", conference));
		list.add(findValueByKeyAndConference("address2", conference));
		list.add(findValueByKeyAndConference("map", conference));
		return list;
	}
	
	public void saveConfig(List<Object> config, Conference conference) {
		updateValueByKeyAndConference("image_path", config.get(0).toString(), conference);
		updateValueByKeyAndConference("logo", config.get(1).toString(), conference);
		updateValueByKeyAndConference("main_photo", config.get(2).toString(), conference);
		updateValueByKeyAndConference("email", config.get(3).toString(), conference);
		updateValueByKeyAndConference("phone", config.get(4).toString(), conference);
		updateValueByKeyAndConference("address1", config.get(5).toString(), conference);
		updateValueByKeyAndConference("address2", config.get(6).toString(), conference);
		updateValueByKeyAndConference("map", config.get(7).toString(), conference);
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
    		return TableData.PATH;
    	}
    	else if(key == "logo") {
    		return "";
    	}
    	else if(key == "main_photo") {
    		return "";
    	}
    	else if(key == "email") {
    		return TableData.EMAIL;
    	}
    	else if(key == "phone") {
    		return TableData.PHONE;
    	}
    	else if(key == "address1") {
    		return TableData.ADDRESS1;
    	}
    	else if(key == "address2") {
    		return TableData.ADDRESS2;
    	}
    	else if(key == "map") {
    		return TableData.MAP;
    	}
    	return "";
    }
}
