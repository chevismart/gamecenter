package gamecenter.core.beans;

/**
 * Created by Chevis on 2015/2/19.
 */
public interface Figure {

    public static Figure MONEY_TO_COIN = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base / 100;
        }
    };

    public static Figure COIN_TO_MONEY = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base * 100;
        }
    };

    public static Figure BUY_1_GET_1_FREE = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base * 2;
        }
    };

    Integer calculate(int base);
}
