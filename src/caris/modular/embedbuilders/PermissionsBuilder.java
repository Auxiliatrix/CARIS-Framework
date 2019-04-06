package caris.modular.embedbuilders;

import java.awt.Color;

import caris.configuration.calibration.Constants;
import caris.configuration.reference.PermissionsString;
import caris.framework.main.Brain;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

public class PermissionsBuilder {

	public static EmbedBuilder permissionsBuilder = new EmbedBuilder()
			.withColor(Color.GREEN)
			.withFooterIcon(Brain.cli.getApplicationIconURL());
	
	public static EmbedObject getPermissionsEmbed(IRole role) {
		permissionsBuilder.clearFields();
		permissionsBuilder.withAuthorIcon(role.getGuild().getIconURL());
		permissionsBuilder.withTitle(role.getName() + " Permissions:");
		for( Permissions permission : role.getPermissions() ) {
			permissionsBuilder.appendDescription(PermissionsString.PERMISSIONS_STRING.get(permission) + "\n");
		}
		return permissionsBuilder.build();
	}
	
	public static EmbedObject getPermissionsEmbed(IUser user, IGuild guild) {
		permissionsBuilder.clearFields();
		permissionsBuilder.withAuthorIcon(user.getAvatarURL());
		permissionsBuilder.withTitle(user.getName() + " Permissions:");
		for( Permissions permission : user.getPermissionsForGuild(guild) ) {
			permissionsBuilder.appendDescription(PermissionsString.PERMISSIONS_STRING.get(permission) + "\n");
		}
		permissionsBuilder.withFooterText(Constants.INVOCATION_PREFIX + "PermissionsView module");
		return permissionsBuilder.build();
	}
}
