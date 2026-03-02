library ieee;
use ieee.std_logic_1164.all;

entity SerialReceiver_tb is
end SerialReceiver_tb;

architecture behavioral of SerialReceiver_tb is

component SerialReceiver is 
	port( 
	SS : in std_logic;
	MCLK : in std_logic;
	SDX : in std_logic;
	RESET : in std_logic;
	SCLK : in std_logic;
	accept : in std_logic;
	D : out std_logic_vector(4 downto 0);
	DXval : out std_logic
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;


signal SDX_tb     : std_logic := '0';
signal SCLK_tb    : std_logic := '0';
signal SS_tb      : std_logic := '1';  
signal accept_tb  : std_logic := '0';
signal RESET_tb   : std_logic := '0';
signal MCLK_tb    : std_logic := '0';
signal D_tb       : std_logic_vector(4 downto 0);
signal DXval_tb   : std_logic;

begin

UUT: SerialReceiver
	port map(
	SS => SS_tb,
	MCLK => MCLK_tb,
	SDX => SDX_tb,
	RESET => RESET_tb,
	SCLK => SCLK_tb,
	accept => accept_tb,
	D => D_tb,
	DXval => DXval_tb
	);


clk_gen : process 
begin 
	MCLK_tb <= '0';
	wait for MCLK_HALF_PERIOD;
	MCLK_tb <= '1';
	wait for MCLK_HALF_PERIOD;
end process;

   stimulus : process
   begin
		  
        Reset_tb <= '1';
        wait for MCLK_PERIOD;
        Reset_tb <= '0';
        wait for MCLK_PERIOD;

-- Start
        SS_tb <= '0';        
        accept_tb <= '1';     
        wait for MCLK_PERIOD;
        accept_tb <= '0';
	wait for MCLK_PERIOD;


-- bit 0
        SDX_tb <= '1'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

-- bit 1
        SDX_tb <= '0'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

-- bit 2
        SDX_tb <= '1'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

-- bit 3
        SDX_tb <= '0'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

-- bit 4
        SDX_tb <= '1'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

-- bit 5 (P)
	SDX_tb <= '0'; 
	wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5; 
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;
	  
	SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5;
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;

        
        SS_tb <= '1';  	  
	wait for MCLK_PERIOD * 5;
		 
       wait;
   end process;

end behavioral;
