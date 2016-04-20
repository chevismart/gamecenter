package gamecenter.core.beans.builders;

public interface TopUpRequestParamsBuilderInterface {
    interface CenterIdBuilder {
        MacAddressBuilder centerId(String centerId);
    }

    interface MacAddressBuilder {
        ReferenceIdBuilder macAddress(String mac);
    }

    interface ReferenceIdBuilder {
        CoinBuilder referenceId(String referenceId);
    }

    interface CoinBuilder {
        Builder coins(int coins);
    }

    interface Builder {
        String build();
    }
}
