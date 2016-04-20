package gamecenter.core.beans;

/**
 * Created by Chevis on 2015/2/19.
 */
public interface Figure {

    Figure[] EMPTY_FIGURE = new Figure[]{};

    Figure MONEY_TO_COIN = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base / 100;
        }
    };

    Figure COIN_TO_MONEY = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base * 100;
        }
    };

    Figure CEN_TO_YUAN =  new Figure() {
        public Integer calculate(int base) {
//            return base;
            return base * 100;
        }
    };

    Figure FIVE_PERCENTAGE_OFF = new Figure() {
        @Override
        public Integer calculate(int base) {
            return (int) Math.floor(base * 0.95);
        }
    };

    Figure BUY_1_GET_1_FREE = new Figure() {
        @Override
        public Integer calculate(int base) {
            return base * 2;
        }
    };

    Integer calculate(int base);
}
