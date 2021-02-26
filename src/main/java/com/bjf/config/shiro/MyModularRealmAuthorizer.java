package com.bjf.config.shiro;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 重写授权
 */
public class MyModularRealmAuthorizer extends ModularRealmAuthorizer {

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission){
        assertRealmsConfigured();
        //所有realm
        Collection<Realm> realms = getRealms();
        HashMap<String,Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            if(realm.getName().contains("UserRealm")){
                realmHashMap.put("UserRealm",realm);
            }else if (realm.getName().contains("BusyRealm")){
                realmHashMap.put("BusyRealm",realm);
            }
        }
        Set<String> realmNames = principals.asSet();

        //System.out.println("========"+realmNames);

        if(realmNames != null){
            String realmName = null;
            Iterator it = realmNames.iterator();
            while (it.hasNext()){
                realmName = ConvertUtils.convert(it.next());
                if(realmName.contains("User")){
                    return ((UserRealm)realmHashMap.get("UserRealm")).isPermitted(principals,permission);
                }else if(realmName.contains("Busy")){
                    return((BusyRealm)realmHashMap.get("BusyRealm")).isPermitted(principals,permission);
                }
                break;
            }
        }
        return false;
    }
}
