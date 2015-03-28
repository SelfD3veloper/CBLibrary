package cbedoy.cblibrary.orm;

import com.orm.SugarRecord;

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
public interface IORMService<cbedoy>
{
    public void insertEntityFromClassWithSettersAndValues
            (
                Class<cbedoy> className,
                Method[] setters,
                Object[] values
            );

    public cbedoy loadEntityFromClassWithId
            (
                Class<cbedoy> className,
                long identifier
            );

    public void  updateEntityFromClassWithIdSettersAndValues
            (
                    Class<cbedoy> className,
                    Method[] setters,
                    Object[] values,
                    long indentifier
            );

    public void deleteEntityFromClassWithId
            (
                    Class<cbedoy> className,
                    long identifier
            );

    public List<cbedoy> requestAllEntitiesFromClass
            (
                    Class<cbedoy> className
            );

}
