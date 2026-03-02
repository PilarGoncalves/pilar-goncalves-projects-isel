library ieee;
use ieee.std_logic_1164.all;

entity KeyboardReader_tb is
end KeyboardReader_tb;

architecture behavioral of KeyboardReader_tb is

component KeyboardReader is 
	port( 
   RESET : in std_logic;
   CLK : in std_logic;
   Dval : out std_logic;
   D : out std_logic_vector (3 downto 0);
   KeyPadL : in std_logic_vector (3 downto 0);
   KeyPadC : out std_logic_vector (3 downto 0);
   ACK : in std_logic
   );
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0';
signal Dval_tb : std_logic;
signal D_tb : std_logic_vector(3 downto 0);
signal KeyPadL_tb : std_logic_vector(3 downto 0) := "1111"; 
signal KeyPadC_tb : std_logic_vector(3 downto 0);
signal ACK_tb : std_logic := '0';

begin

UUT: KeyboardReader
	port map(
   RESET => RESET_tb,
   CLK => CLK_tb,
   Dval => Dval_tb,
   D => D_tb,
   KeyPadL => KeyPadL_tb,
   KeyPadC => KeyPadC_tb,
   ACK => ACK_tb
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

       -- Espera após reset
       wait for MCLK_PERIOD * 20;

       -- 
       KeyPadL_tb <= "1110";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1101";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1100";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1011";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1010";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1001";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "1000";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0111";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0110";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0101";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0100";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0011";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0010";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0001";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0000";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0010";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
		 KeyPadL_tb <= "0101";  
       wait for MCLK_PERIOD * 50;
		 KeyPadL_tb <= "1111";
       wait for MCLK_PERIOD * 5;
		 
       -- Simula reconhecimento da tecla (ACK ativo)
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 ---
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
		 
       ACK_tb <= '1';
       wait for MCLK_PERIOD * 5;
       ACK_tb <= '0';
       wait for MCLK_PERIOD * 5;
       wait;
   end process;

end behavioral;