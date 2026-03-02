library ieee;
use ieee.std_logic_1164.all;

entity OutputRegister_tb is
end OutputRegister_tb;

architecture behavioral of OutputRegister_tb is

    component OutputRegister
        port(
            CLK   : in std_logic;
            RESET : in std_logic;
            D     : in std_logic_vector(3 downto 0);
            EN    : in std_logic;
            Q     : out std_logic_vector(3 downto 0)
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal CLK_tb     : std_logic := '0';
    signal RESET_tb   : std_logic := '0';
    signal D_tb       : std_logic_vector(3 downto 0) := "0000";
    signal EN_tb      : std_logic := '0';
    signal Q_tb       : std_logic_vector(3 downto 0);

begin

    UUT: OutputRegister port map(
        CLK   => CLK_tb,
        RESET => RESET_tb,
        D     => D_tb,
        EN    => EN_tb,
        Q     => Q_tb
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

        RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;

        D_tb <= "1010";
        EN_tb <= '1';
        wait for MCLK_PERIOD;

        D_tb <= "1111";
        EN_tb <= '0';
        wait for MCLK_PERIOD;

        D_tb <= "0101";
        EN_tb <= '1';
        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
