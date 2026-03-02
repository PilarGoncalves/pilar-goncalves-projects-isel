library ieee;
use ieee.std_logic_1164.all;

entity Roulette_Dispatcher_tb is
end Roulette_Dispatcher_tb;

architecture behavioral of Roulette_Dispatcher_tb is

component Roulette_Dispatcher is
	port(
	Dval   : in std_logic;
	CLK    : in std_logic;
	RESET  : in std_logic;
	done   : out std_logic;
	WrR    : out std_logic;
	Din    : in std_logic_vector(7 downto 0);
	Dout   : out std_logic_vector(7 downto 0)
        );
end component;

constant MCLK_PERIOD      : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal Dval_tb  : std_logic := '0';
signal CLK_tb   : std_logic := '0';
signal RESET_tb : std_logic := '0';
signal done_tb  : std_logic;
signal WrR_tb   : std_logic;
signal Din_tb   : std_logic_vector(7 downto 0) := "10101010";
signal Dout_tb  : std_logic_vector(7 downto 0);

begin

    UUT: Roulette_Dispatcher
        port map(
            Dval => Dval_tb,
            CLK => CLK_tb,
            RESET => RESET_tb,
            done => done_tb,
            WrR => WrR_tb,
            Din => Din_tb,
            Dout => Dout_tb
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
        wait for MCLK_PERIOD * 2;

        Dval_tb <= '1';
        wait for MCLK_PERIOD * 4;
        Dval_tb <= '0';
        wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
