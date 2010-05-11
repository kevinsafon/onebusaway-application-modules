package org.onebusaway.users.impl.validation;

import org.onebusaway.users.services.validation.KeyValidationProvider;
import org.onebusaway.users.services.validation.KeyValidationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyValidationServiceImpl implements KeyValidationService {

  private static Set<String> _keys = new HashSet<String>() {
    private static final long serialVersionUID = 1L;
    {
      add("TEST");
      add("org.onebusaway.iphone");
      add("org.onebusaway.nokia");
      add("edu.washington.cs.cse403b");
    }
  };

  private Map<String, KeyValidationProvider> _providers = new HashMap<String, KeyValidationProvider>();

  private String _defaultProviderId;

  public void setDefaultProviderId(String defaultId) {
    _defaultProviderId = defaultId;
  }

  public void setProviders(Collection<KeyValidationProvider> providers) {
    for (KeyValidationProvider provider : providers)
      addProvider(provider);
  }

  public void addProvider(KeyValidationProvider provider) {
    if (_providers.containsKey(provider.getId()))
      throw new IllegalStateException("duplicate provider: " + provider.getId());
    _providers.put(provider.getId(), provider);
  }

  /****
   * {@link KeyValidationService} Interface
   ****/

  @Override
  public String generateKey(String value) {
    return generateKey(_defaultProviderId, value);
  }

  @Override
  public String generateKey(String providerId, String value) {
    KeyValidationProvider provider = _providers.get(providerId);
    if (provider == null)
      throw new IllegalStateException("no api key validation provider with id="
          + providerId);
    return providerId + "_" + provider.generateKey(value);
  }

  @Override
  public boolean isValidKey(String key) {
    if (_keys.contains(key))
      return true;
    int index = key.indexOf('_');
    if (index == -1)
      return false;
    String providerId = key.substring(0, index);
    String subKey = key.substring(index + 1);
    KeyValidationProvider provider = _providers.get(providerId);
    if (provider == null)
      return false;
    return provider.isValidKey(subKey);
  }

  @Override
  public Map<String, String> getKeyInfo(String key) {
    
    Map<String,String> info = new HashMap<String, String>();
    info.put("key",key);
    
    if (_keys.contains(key)) {
      info.put("builtin","true");
      return info;
    }
    
    int index = key.indexOf('_');
    
    if (index == -1) {
      info.put("malformed","true");
      return info;
    }
      
    String providerId = key.substring(0, index);
    String subKey = key.substring(index + 1);
    KeyValidationProvider provider = _providers.get(providerId);
    if (provider == null) {
      info.put("malformed","true");
      return info;
    }
    
    provider.getKeyInfo(subKey,info);
    
    return info;
  }
}