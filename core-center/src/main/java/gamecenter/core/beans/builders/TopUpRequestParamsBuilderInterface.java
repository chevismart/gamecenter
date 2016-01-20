package gamecenter.core.beans.builders;

/**
 * Created by Chevis on 15/4/1.
 */
public interface TopUpRequestParamsBuilderInterface {
    public interface CenterIdBuilder {
        public MacAddressBuilder centerId(String centerId);
    }

    public interface MacAddressBuilder {
        public ReferenceIdBuilder macAddress(String mac);
    }

    public static interface ReferenceIdBuilder {
        public CoinBuilder referenceId(String referenceId);
    }

    public interface CoinBuilder {
        public Builder coins(int coins);
    }

    public interface Builder {
        public String build();
    }
}
