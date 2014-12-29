package cbedoy.cblibrary.business;


import cbedoy.cblibrary.interfaces.IBackCore;
import cbedoy.cblibrary.interfaces.IMementoHandler;
import cbedoy.cblibrary.interfaces.IMessageRepresentationHandler;

/**
 * Created by Carlos Bedoy on 28/12/2014.
 *
 * Mobile App Developer
 * CBLibrary
 *
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public abstract class BusinessController implements IBackCore
{
    protected IMementoHandler mementoHandler;
    protected IMessageRepresentationHandler messageRepresentationHandler;

    public void setMementoHandler(IMementoHandler mementoHandler) {
        this.mementoHandler = mementoHandler;
    }

    public void setMessageRepresentationHandler(IMessageRepresentationHandler messageRepresentationHandler) {
        this.messageRepresentationHandler = messageRepresentationHandler;
    }

    @Override
    public void backRequested() {
        mementoHandler.popDataFor(this);
    }
}
