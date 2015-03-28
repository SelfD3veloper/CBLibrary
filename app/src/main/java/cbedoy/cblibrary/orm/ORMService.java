package cbedoy.cblibrary.orm;

import com.orm.SugarRecord;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Carlos Bedoy on 27/03/2015.
 * <p/>
 * Mobile App Developer
 * My Fuel Inversion
 * <p/>
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class ORMService<T> implements IORMService
{


    @Override
    public void insertEntityFromClassWithSettersAndValues(Class className, Method[] setters, Object[] values) {
        try {
            Object handler = className.newInstance();
            for(int i=0; i<setters.length; i++){
                Method setter = setters[i];
                Object value = values[i];
                setter.invoke(handler, value);
            }
            SugarRecord sugarRecord = (SugarRecord) handler;
            sugarRecord.save();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object loadEntityFromClassWithId(Class className, long identifier)
    {
        return SugarRecord.findById(className, identifier);
    }

    @Override
    public void updateEntityFromClassWithIdSettersAndValues(Class className, Method[] setters, Object[] values, long identifier) {
        try {
            SugarRecord sugarRecord = SugarRecord.findById(className, identifier);
            for(int i=0; i<setters.length; i++){
                Method setter = setters[i];
                Object value = values[i];
                setter.invoke(sugarRecord, value);
            }
            sugarRecord.save();
        } catch ( IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEntityFromClassWithId(Class className, long identifier) {
        SugarRecord sugarRecord = SugarRecord.findById(className, identifier);
        sugarRecord.delete();
    }

    @Override
    public List requestAllEntitiesFromClass(Class className) {
        return SugarRecord.listAll(className);
    }
}
