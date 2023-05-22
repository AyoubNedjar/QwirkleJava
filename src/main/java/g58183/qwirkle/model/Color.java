package g58183.qwirkle.model;

/**
 *
 * Color represent color of tile.
 */
public enum Color {

        BLUE("\033[0;34m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        ORANGE("\u001B[38;5;208m"),
        YELLOW("\033[0;33m"),
        PURPLE("\033[0;35m");

        private final String colorChar;

        Color(String colorChar) {
                this.colorChar = colorChar;
        }

        @Override
        public String toString(){
                return this.colorChar;
        }
}
