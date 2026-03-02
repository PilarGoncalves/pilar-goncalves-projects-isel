library ieee;
use ieee.std_logic_1164.all;

entity comparator_MAC_tb is
end comparator_MAC_tb;

architecture behavioral of comparator_MAC_tb is

    component comparator_MAC
        port (
            A  : in std_logic_vector(4 downto 0);
            B  : in std_logic_vector(4 downto 0);
            TC : out std_logic
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal A_tb  : std_logic_vector(3 downto 0) := "00000";
    signal B_tb  : std_logic_vector(3 downto 0) := "00000";
    signal TC_tb : std_logic;
    signal CLK_tb : std_logic := '0';

begin

    UUT: comparator_MAC port map(
        A  => A_tb,
        B  => B_tb,
        TC => TC_tb
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
        -- A = B
        A_tb <= "00000"; B_tb <= "00000";
        wait for MCLK_PERIOD;

        -- A != B
        A_tb <= "00001"; B_tb <= "00000";
        wait for MCLK_PERIOD;

        -- A = B
        A_tb <= "10101"; B_tb <= "10101";
        wait for MCLK_PERIOD;

        -- A != B
        A_tb <= "11111"; B_tb <= "00000";
        wait for MCLK_PERIOD;

        -- A != B
        A_tb <= "01010"; B_tb <= "01110";
        wait for MCLK_PERIOD;

        -- A = B
        A_tb <= "11111"; B_tb <= "11111";
        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
