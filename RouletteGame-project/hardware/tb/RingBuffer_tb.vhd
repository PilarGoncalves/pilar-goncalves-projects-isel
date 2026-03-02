library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer_tb is
end RingBuffer_tb;

architecture behavioral of RingBuffer_tb is

component RingBuffer is
	port(
	D : in std_logic_vector(3 downto 0);
	DAV : in std_logic;
	CTS : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	Wreg : out std_logic;
	DAC : out std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal D_tb: std_logic_vector (3 downto 0) := "0000"; 
signal Q_tb: std_logic_vector (3 downto 0);
signal DAV_tb, CTS_tb, CLK_tb, RESET_tb: std_logic := '0';
signal Wreg_tb, DAC_tb: std_logic;

begin

UUT: RingBuffer port map(  
                      D   => D_tb,
                      DAV  => DAV_tb, 
                      CTS  => CTS_tb,
                      CLK  => CLK_tb,
                      RESET=> RESET_tb,
                      Q    => Q_tb,
							 Wreg => Wreg_tb,
							 DAC  => DAC_tb
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
	 
		-- Escrita 1
		D_tb   <= "0001";
		DAV_tb <= '1';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		DAV_tb <= '0';
		wait for MCLK_PERIOD;

		-- Escrita 2
		D_tb   <= "0010";
		DAV_tb <= '1';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		DAV_tb <= '0';
		wait for MCLK_PERIOD;

		-- Escrita 3
		D_tb   <= "0011";
		DAV_tb <= '1';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
		DAV_tb <= '0';
		wait for MCLK_PERIOD;
	 
	   -- Leitura 1
		DAV_tb    <= '0';
		CTS_tb    <= '1';
		wait for MCLK_PERIOD;
		CTS_tb    <= '0';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
	 
	   -- Leitura 2
		DAV_tb    <= '0';
		CTS_tb    <= '1';
		wait for MCLK_PERIOD;
		CTS_tb    <= '0';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
	 
	   -- Leitura 3
		DAV_tb    <= '0';
		CTS_tb    <= '1';
		wait for MCLK_PERIOD;
		CTS_tb    <= '0';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;

		-- Leitura extra (para verificar empty)
		DAV_tb  <= '0';
		CTS_tb   <= '1';
		wait for MCLK_PERIOD;
		CTS_tb <= '0';
		wait for MCLK_PERIOD;
		wait for MCLK_PERIOD;
	 
	  wait;
     
end process;

end behavioral;