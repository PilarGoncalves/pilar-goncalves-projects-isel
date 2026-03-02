library ieee;
use ieee.std_logic_1164.all;

entity Roulette_Game is 
	port( 
	RESET :  in std_logic;
	CLK : in std_logic;
	KeyPadL : in std_logic_vector (3 downto 0);
	Coin : in std_logic;
	Coinid : in std_logic;
	M : in std_logic;
	KeyPadC : out std_logic_vector (3 downto 0);
	Dval : out std_logic;
	D : out std_logic_vector (3 downto 0);
	RS : out std_logic;
	E : out std_logic;
	DLCD : out std_logic_vector (7 downto 4);
	HEX0	: out std_logic_vector(7 downto 0);
	HEX1	: out std_logic_vector(7 downto 0);
	HEX2	: out std_logic_vector(7 downto 0);
	HEX3	: out std_logic_vector(7 downto 0);
	HEX4	: out std_logic_vector(7 downto 0);
	HEX5	: out std_logic_vector(7 downto 0);
	accept : out std_logic
	);
end Roulette_Game; 	
	
architecture structural of Roulette_Game is

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

component SLCDC is
	port(
	SS: in std_logic;
	SDX: in std_logic;
	SCLK:in std_logic;
	MCLK: in std_logic;
	RESET: in std_logic;
	WrL:out std_logic;
	Dout: out std_logic_vector(4 downto 0)
	);
end component;

component rouletteDisplay is
port(	set	: in std_logic;
		cmd	: in std_logic_vector(2 downto 0);
		data	: in std_logic_vector(4 downto 0);
		HEX0	: out std_logic_vector(7 downto 0);
		HEX1	: out std_logic_vector(7 downto 0);
		HEX2	: out std_logic_vector(7 downto 0);
		HEX3	: out std_logic_vector(7 downto 0);
		HEX4	: out std_logic_vector(7 downto 0);
		HEX5	: out std_logic_vector(7 downto 0)
		);
end component;

component SRC is 
	port(
	SS: in std_logic;
	SDX: in std_logic;
	SCLK:in std_logic;
	MCLK: in std_logic;
	RESET: in std_logic;
	WrD:out std_logic;
	Dout: out std_logic_vector(7 downto 0)
	);
end component;

component UsbPort is
	port(
		inputPort:  IN  STD_LOGIC_VECTOR(7 DOWNTO 0);
		outputPort :  OUT  STD_LOGIC_VECTOR(7 DOWNTO 0)
	);
end component;

component Register1 is
port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(3 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end component;

signal Dval_usb : std_logic; 
signal D_usb : std_logic_vector(3 downto 0);
signal usb_ACK : std_logic; 

signal DLCD_exit : std_logic_vector(7 downto 4);
signal E_exit : std_logic; 
signal RS_exit : std_logic; 

signal usb_SS_SRC_reg : std_logic; 
signal usb_SS_reg : std_logic; 
signal usb_SCLK_reg : std_logic; 
signal usb_SDX_reg : std_logic; 

signal reg_SS_SRC : std_logic; 
signal reg_SS : std_logic; 
signal reg_SCLK : std_logic; 
signal reg_SDX : std_logic;

signal WrD_set : std_logic;
signal D_cmd : std_logic_vector(2 downto 0);
signal D_data: std_logic_vector(4 downto 0);

begin
U0: KeyboardReader port map ( 
			CLK => CLK, 
			RESET => RESET, 
			Dval => Dval_usb, 
			D(0) => D_usb(0), 
			D(1) => D_usb(1), 
			D(2) => D_usb(2), 
			D(3) => D_usb(3), 
			KeyPadL(0) => KeyPadL(0), 
			KeyPadL(1) => KeyPadL(1), 
			KeyPadL(2) => KeyPadL(2), 
			KeyPadL(3) => KeyPadL(3), 
			KeyPadC(0) => KeyPadC(0), 
			KeyPadC(1) => KeyPadC(1), 
			KeyPadC(2) => KeyPadC(2), 
			KeyPadC(3) => KeyPadC(3) , 
			ACK => usb_ACK );

U1: UsbPort port map( 
	inputPort(4) => Dval_usb, 
	inputPort(0) => D_usb(0), 
	inputPort(1) => D_usb(1),
	inputPort(2) => D_usb(2), 
	inputPort(3) => D_usb(3),
	inputPort(5) => Coinid,
	inputPort(6) => Coin,
	inputPort(7) => M,
	outputPort(7) => usb_ACK,
	outputPort(6) => accept,
	outputPort(3) => usb_SS_SRC_reg,
	outputPort(2) => usb_SS_reg,
	outputPort(1) => usb_SCLK_reg,
	outputPort(0) => usb_SDX_reg);
	
	
U2: SLCDC port map(
			SS => reg_SS,
			SDX => reg_SDX,
			SCLK => reg_SCLK,
			MCLK => CLK,
			RESET => RESET,
			WrL => E_exit,
			Dout(0) => RS_exit,
			Dout(1) => DLCD_exit(4),
			Dout(2) => DLCD_exit(5),
			Dout(3) => DLCD_exit(6),
			Dout(4) => DLCD_exit(7));
			
U3: rouletteDisplay port map(
			set => WrD_set,
			cmd => D_cmd,
			data => D_data,
			HEX0 => HEX0, 
			HEX1 => HEX1,
			HEX2 => HEX2,
			HEX3 => HEX3,
			HEX4 => HEX4,
			HEX5 => HEX5);

U4: SRC port map(
		   SS => reg_SS_SRC,
			SDX => reg_SDX,
			SCLK => reg_SCLK,
			MCLK => CLK,
			RESET => RESET,
			WrD => WrD_set,
			Dout(0) => D_cmd(0),
			Dout(1) => D_cmd(1),
			Dout(2) => D_cmd(2),
			Dout(3) => D_data(0),
			Dout(4) => D_data(1),
			Dout(5) => D_data(2),
			Dout(6) => D_data(3),
			Dout(7) => D_data(4) );
			
U5: Register1 port map(
			CLK => CLK,
			RESET => RESET,
			EN => '1',
			D(0) => usb_SDX_reg,
			D(1) => usb_SCLK_reg,
			D(2) => usb_SS_reg,
			D(3) => usb_SS_SRC_reg,
			Q(0) => reg_SDX,
			Q(1) => reg_SCLK,
			Q(2) => reg_SS,
			Q(3) => reg_SS_SRC );

D <= D_usb;
Dval <= Dval_usb;

RS <= RS_exit;
E <= E_exit;
DLCD <= DLCD_exit;

end structural;


