library ieee;
use ieee.std_logic_1164.all;

entity CounterUD_MAC_tb is
end CounterUD_MAC_tb;

architecture behavioral of CounterUD_MAC_tb is

    component CounterUD_MAC
        port (
            CE       : in std_logic;
            CLK      : in std_logic;
            RESET    : in std_logic;
            UP_DOWN  : in std_logic;
            Q        : out std_logic_vector(4 downto 0)
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal CE_tb      : std_logic := '0';
    signal CLK_tb     : std_logic := '0';
    signal RESET_tb   : std_logic := '0';
    signal UP_DOWN_tb : std_logic := '0';
    signal Q_tb       : std_logic_vector(4 downto 0);

begin

    UUT: CounterUD_MAC port map(
        CE      => CE_tb,
        CLK     => CLK_tb,
        RESET   => RESET_tb,
        UP_DOWN => UP_DOWN_tb,
        Q       => Q_tb
    );

    clk_gen : process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stimulus : process
    begin
        -- Reset inicial
        RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;

        CE_tb <= '1';
        UP_DOWN_tb <= '0';

        wait for MCLK_PERIOD; -- Q = 00001
        wait for MCLK_PERIOD; -- Q = 00010
        wait for MCLK_PERIOD; -- Q = 00011

        -- Parar contagem (CE = 0)
        CE_tb <= '0';
        wait for MCLK_PERIOD; -- Q deve manter-se
        wait for MCLK_PERIOD;

        CE_tb <= '1';
        UP_DOWN_tb <= '1';

        wait for MCLK_PERIOD; -- Q = 00010
        wait for MCLK_PERIOD; -- Q = 00001
        wait for MCLK_PERIOD; -- Q = 00000

        -- CE = 0 novamente => nada deve mudar
        CE_tb <= '0';
        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
