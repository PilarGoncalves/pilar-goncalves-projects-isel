library ieee;
use ieee.std_logic_1164.all;

entity Counter_tb is
end Counter_tb;

architecture behavioral of Counter_tb is

component Counter is 
	port (
	CE,CLK,RESET : in std_logic ; 
	Q: out std_logic_vector (3 downto 0) 
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal CE_tb : std_logic;
signal CLK_tb : std_logic;
signal RESET_tb : std_logic;
signal Q_tb : std_logic_vector (3 downto 0);

begin

UUT: Counter
		port map(CE => CE_tb,
					CLK => CLK_tb,
					RESET => RESET_tb,
					Q => Q_tb);
			
clk_gen : process 
begin 
		CLK_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		CLK_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;
					
stimulus: process
begin

	CE_tb <= '0';
	RESET_tb <= '1';
	wait for MCLK_PERIOD;
	RESET_tb <= '0';
	
	CE_tb <= '1';
	wait for MCLK_PERIOD*5;
	
	CE_tb <= '0';
	wait for MCLK_PERIOD*3;
	
	RESET_tb <= '1';
	wait for MCLK_PERIOD;
	RESET_tb <= '0';

	wait;

end process;
end behavioral;
