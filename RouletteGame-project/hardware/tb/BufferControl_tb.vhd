library ieee;
use ieee.std_logic_1164.all;

entity BufferControl_tb is
end BufferControl_tb;

architecture behavioral of BufferControl_tb is

    component BufferControl
        port(
            Load   : in std_logic;
            ACK    : in std_logic;
            RESET  : in std_logic;
            CLK    : in std_logic;
            Wreg   : out std_logic;
            OBfree : out std_logic;
            Dval   : out std_logic
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal Load_tb    : std_logic := '0';
    signal ACK_tb     : std_logic := '0';
    signal RESET_tb   : std_logic := '0';
    signal CLK_tb     : std_logic := '0';
    signal Wreg_tb    : std_logic;
    signal OBfree_tb  : std_logic;
    signal Dval_tb    : std_logic;

begin

    UUT: BufferControl port map(
        Load   => Load_tb,
        ACK    => ACK_tb,
        RESET  => RESET_tb,
        CLK    => CLK_tb,
        Wreg   => Wreg_tb,
        OBfree => OBfree_tb,
        Dval   => Dval_tb
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

        wait for MCLK_PERIOD;

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
