package ru.nikita.platform.sms.providers;

import com.redshape.utils.events.AbstractEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderEvent extends AbstractEvent {
    public static class Type {
        private String code;

        protected Type( String code ) {
            this.code = code;
        }

        public static class Connection extends Type {
            
            protected Connection( String code ) {
                super(code);
            }
            
            public static class SMPP extends Connection {
                
                protected SMPP( String code ) {
                    super(code);
                }
                
                public static final Connection ReceivedPDU = new Connection("ProviderEvent.Connection.SMPP.ReceivedPDU");
                
            }
            
            public static final Connection Bind = new Connection("ProviderEvent.Connection.Bind");
            public static final Connection Disconnect = new Connection("ProviderEvent.Connection.Disconnect");
            
        }
        
        public static class Message extends Type {

            protected Message( String code ) {
                super(code);
            }

            public static final Message Sent = new Message("ProcessorEvent.Type.Message.Send");
            public static final Message Received = new Message("ProcessorEvent.Type.Message.Received");

        }

        @Override
        public int hashCode() {
            return this.code.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj == null || obj != this ) {
                return false;
            }
            
            if ( !(obj instanceof Type) ) {
                return false;
            }

            return ( (Type) obj ).code.equals( this.code );
        }
    }

    private IProvider provider;
    private Type type;
    
    public ProviderEvent( IProvider provider, Type type ) {
        this(provider, type, new Object[] {} );
    }
    
    public ProviderEvent( IProvider provider, Type type, Object... args ) {
        super(args);

        this.provider = provider;
        this.type = type;
    }

    public IProvider getProvider() {
        return provider;
    }

    public Type getType() {
        return type;
    }
}
