library ieee;
use ieee.std_logic_1164.all;

entity OutputBuffer_tb is
end OutputBuffer_tb;

architecture behavioral of OutputBuffer_tb is

    component OutputBuffer
        port(
            D      : in std_logic_vector(3 downto 0);
            Load   : in std_logic;
            ACK    : in std_logic;
            RESET  : in std_logic;
            CLK    : in std_logic;
            OBfree : out std_logic;
            Dval   : out std_logic;
            Q      : out std_logic_vector(3 downto 0)
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal D_tb       : std_logic_vector(3 downto 0) := "0000";
    signal Load_tb    : std_logic := '0';
    signal ACK_tb     : std_logic := '0';
    signal RESET_tb   : std_logic := '0';
    signal CLK_tb     : std_logic := '0';
    signal OBfree_tb  : std_logic;
    signal Dval_tb    : std_logic;
    signal Q_tb       : std_logic_vector(3 downto 0);

begin

    UUT: OutputBuffer port map(
        D      => D_tb,
        Load   => Load_tb,
        ACK    => ACK_tb,
        RESET  => RESET_tb,
        CLK    => CLK_tb,
        OBfree => OBfree_tb,
        Dval   => Dval_tb,
        Q      => Q_tb
    );

    clk_gen : process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;


    stimulus: process
    begin

        RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;

        D_tb   <= "1010";
        Load_tb <= '1';
        wait for MCLK_PERIOD;
        Load_tb <= '0';
        wait for MCLK_PERIOD;

        wait for MCLK_PERIOD;

        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
