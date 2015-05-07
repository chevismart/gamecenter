package gamecenter.core.beans.builders;

/**
 * Created by Chevis on 15/4/1.
 */
public interface TopUpRequestParamsBuilderInterface {
    public interface CenterIdBuilder {
        public TokenBuilder centerId(String centerId);
    }

    public static interface TokenBuilder {
        public MacAddressBuilder token(String token);
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
