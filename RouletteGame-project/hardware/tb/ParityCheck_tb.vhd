library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck_tb is
end entity;

architecture behavioral of ParityCheck_tb is

    component ParityCheck is
        port (
            data  : in std_logic;
            init  : in std_logic;
            CLK   : in std_logic;
            err   : out std_logic
        );
    end component;

    constant MCLK_PERIOD : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal CLK_tb   : std_logic := '0';
    signal data_tb  : std_logic := '0';
    signal init_tb  : std_logic := '0';
    signal err_tb   : std_logic;

begin

    UUT: ParityCheck
        port map (
            data => data_tb,
            init => init_tb,
            CLK => CLK_tb,
            err => err_tb
        );

    clk_gen: process
    begin
        CLK_tb <= '0';
		  wait for MCLK_PERIOD;
		  CLK_tb <= '1';
		  wait for MCLK_PERIOD;
    end process;

    stimulus: process
    begin
        init_tb <= '1';
        wait for MCLK_PERIOD;
        init_tb <= '0';
	     wait for MCLK_PERIOD;

        -- 1,0,1 (2 bits a 1 → paridade par → err = '1')
        data_tb <= '1';
        wait for MCLK_PERIOD;
        data_tb <= '0';
        wait for MCLK_PERIOD;
        data_tb <= '1';
        wait for MCLK_PERIOD;

        wait for MCLK_PERIOD;

        -- 1,1,1 (3 bits a 1 → paridade ímpar → err = '0')
        data_tb <= '1';
        wait for MCLK_PERIOD;
        data_tb <= '1';
        wait for MCLK_PERIOD;
        data_tb <= '1';
        wait for MCLK_PERIOD;

        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;